package com.itechart.sample.security.acl.jdbc;

import com.itechart.sample.security.acl.AclImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.util.FieldUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides a base JDBC implementation of {@link AclService} and {@link MutableAclService}
 *
 * @author andrei.samarou
 */
public class JdbcAclService extends JdbcDaoSupport implements AclService, MutableAclService {


    private PermissionFactory permissionFactory;


    private int batchSize = 50;


    private final Field fieldAces = FieldUtils.getField(AclImpl.class, "aces");
    private final Field fieldAcl = FieldUtils.getField(AccessControlEntryImpl.class,
            "acl");

    private AclCache aclCache;

    @Override
    protected void checkDaoConfig() {
        super.checkDaoConfig();
        Assert.notNull(aclCache, "AclCache required");
    }

    // ~ Methods
    // ========================================================================================================

    private String computeRepeatingSql(String repeatingSql, int requiredRepetitions) {
        assert requiredRepetitions > 0 : "requiredRepetitions must be > 0";

        String selectClause = "select acl_object_identity.object_id_identity, "
                + "acl_entry.ace_order,  "
                + "acl_object_identity.id as acl_id, "
                + "acl_object_identity.parent_object, "
                + "acl_object_identity.entries_inheriting, "
                + "acl_entry.id as ace_id, "
                + "acl_entry.mask,  "
                + "acl_entry.granting,  "
                + "acl_entry.audit_success, "
                + "acl_entry.audit_failure,  "
                + "acl_sid.principal as ace_principal, "
                + "acl_sid.sid as ace_sid,  "
                + "acli_sid.principal as acl_principal, "
                + "acli_sid.sid as acl_sid, "
                + "acl_class.class "
                + "from acl_object_identity "
                + "left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid "
                + "left join acl_class on acl_class.id = acl_object_identity.object_id_class   "
                + "left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity "
                + "left join acl_sid on acl_entry.sid = acl_sid.id  " + "where ( ";

        String orderByClause = ") order by acl_object_identity.object_id_identity"
                + " asc, acl_entry.ace_order asc";

        StringBuilder sqlStringBldr = new StringBuilder(selectClause.length()
                + orderByClause.length() + requiredRepetitions * (repeatingSql.length() + 4));
        sqlStringBldr.append(selectClause);

        for (int i = 1; i <= requiredRepetitions; i++) {
            sqlStringBldr.append(repeatingSql);

            if (i != requiredRepetitions) {
                sqlStringBldr.append(" or ");
            }
        }

        sqlStringBldr.append(orderByClause);

        return sqlStringBldr.toString();
    }

    @SuppressWarnings("unchecked")
    private List<AccessControlEntryImpl> readAces(AclImpl acl) {
        try {
            return (List<AccessControlEntryImpl>) fieldAces.get(acl);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not obtain AclImpl.aces field", e);
        }
    }

    private void setAclOnAce(AccessControlEntryImpl ace, AclImpl acl) {
        try {
            fieldAcl.set(ace, acl);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    "Could not or set AclImpl on AccessControlEntryImpl fields", e);
        }
    }

    private void setAces(AclImpl acl, List<AccessControlEntryImpl> aces) {
        try {
            fieldAces.set(acl, aces);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not set AclImpl entries", e);
        }
    }

    /**
     * Locates the primary key IDs specified in "findNow", adding AclImpl instances with
     * StubAclParents to the "acls" Map.
     *
     * @param acls    the AclImpls (with StubAclParents)
     * @param findNow Long-based primary keys to retrieve
     * @param sids List of sids
     */
    private void lookupPrimaryKeys(final Map<Serializable, Acl> acls,
                                   final Set<Long> findNow, final List<Sid> sids) {
        Assert.notNull(acls, "ACLs are required");
        Assert.notEmpty(findNow, "Items to find now required");

        String lookupPrimaryKeysWhereClause = "(acl_object_identity.id = ?)";
        String sql = computeRepeatingSql(lookupPrimaryKeysWhereClause, findNow.size());

        Set<Long> parentsToLookup = getJdbcTemplate().query(sql,
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps) throws SQLException {
                        int i = 0;

                        for (Long toFind : findNow) {
                            i++;
                            ps.setLong(i, toFind);
                        }
                    }
                }, new ProcessResultSet(acls, sids));

        // Lookup the parents, now that our JdbcTemplate has released the database
        // connection (SEC-547)
        if (parentsToLookup.size() > 0) {
            lookupPrimaryKeys(acls, parentsToLookup, sids);
        }
    }

    /**
     * The main method.
     * <p>
     * WARNING: This implementation completely disregards the "sids" argument! Every item
     * in the cache is expected to contain all SIDs. If you have serious performance needs
     * (e.g. a very large number of SIDs per object identity), you'll probably want to
     * develop a custom {@link LookupStrategy} implementation instead.
     * <p>
     * The implementation works in batch sizes specified by {@link #batchSize}.
     *
     * @param objects the identities to lookup (required)
     * @param sids    the SIDs for which identities are required (ignored by this
     *                implementation)
     * @return a <tt>Map</tt> where keys represent the {@link ObjectIdentity} of the
     * located {@link Acl} and values are the located {@link Acl} (never <tt>null</tt>
     * although some entries may be missing; this method should not throw
     * {@link NotFoundException}, as a chain of {@link LookupStrategy}s may be used to
     * automatically create entries if required)
     */
    public final Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects,
                                                       List<Sid> sids) {
        Assert.isTrue(batchSize >= 1, "BatchSize must be >= 1");
        Assert.notEmpty(objects, "Objects to lookup required");

        // Map<ObjectIdentity,Acl>
        Map<ObjectIdentity, Acl> result = new HashMap<>(); // contains
        // FULLY
        // loaded
        // Acl
        // objects

        Set<ObjectIdentity> currentBatchToLoad = new HashSet<>();

        for (int i = 0; i < objects.size(); i++) {
            final ObjectIdentity oid = objects.get(i);
            boolean aclFound = false;

            // Check we don't already have this ACL in the results
            if (result.containsKey(oid)) {
                aclFound = true;
            }

            // Check cache for the present ACL entry
            if (!aclFound) {
                Acl acl = aclCache.getFromCache(oid);

                // Ensure any cached element supports all the requested SIDs
                // (they should always, as our base impl doesn't filter on SID)
                if (acl != null) {
                    if (acl.isSidLoaded(sids)) {
                        result.put(acl.getObjectIdentity(), acl);
                        aclFound = true;
                    } else {
                        throw new IllegalStateException(
                                "Error: SID-filtered element detected when implementation does not perform SID filtering "
                                        + "- have you added something to the cache manually?");
                    }
                }
            }

            // Load the ACL from the database
            if (!aclFound) {
                currentBatchToLoad.add(oid);
            }

            // Is it time to load from JDBC the currentBatchToLoad?
            if ((currentBatchToLoad.size() == this.batchSize)
                    || ((i + 1) == objects.size())) {
                if (currentBatchToLoad.size() > 0) {
                    Map<ObjectIdentity, Acl> loadedBatch = lookupObjectIdentities(
                            currentBatchToLoad, sids);

                    // Add loaded batch (all elements 100% initialized) to results
                    result.putAll(loadedBatch);

                    // Add the ACL to the cache
                    for (ObjectIdentity identity : currentBatchToLoad) {
                        Acl loadedAcl = loadedBatch.get(identity);

                        aclCache.putInCache((MutableAcl) loadedAcl);
                    }

                    currentBatchToLoad.clear();
                }
            }
        }

        return result;
    }

    /**
     * Looks up a batch of <code>ObjectIdentity</code>s directly from the database.
     * <p>
     * The caller is responsible for optimization issues, such as selecting the identities
     * to lookup, ensuring the cache doesn't contain them already, and adding the returned
     * elements to the cache etc.
     * <p>
     * This subclass is required to return fully valid <code>Acl</code>s, including
     * properly-configured parent ACLs.
     */
    private Map<ObjectIdentity, Acl> lookupObjectIdentities(
            final Collection<ObjectIdentity> objectIdentities, List<Sid> sids) {
        Assert.notEmpty(objectIdentities, "Must provide identities to lookup");

        final Map<Serializable, Acl> acls = new HashMap<>(); // contains
        // Acls
        // with
        // StubAclParents

        // Make the "acls" map contain all requested objectIdentities
        // (including markers to each parent in the hierarchy)
        String lookupObjectIdentitiesWhereClause = "(acl_object_identity.object_id_identity = ? and acl_class.class = ?)";
        String sql = computeRepeatingSql(lookupObjectIdentitiesWhereClause,
                objectIdentities.size());

        Set<Long> parentsToLookup = getJdbcTemplate().query(sql,
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement ps) throws SQLException {
                        int i = 0;
                        for (ObjectIdentity oid : objectIdentities) {
                            // Determine prepared statement values for this iteration
                            String type = oid.getType();

                            // No need to check for nulls, as guaranteed non-null by
                            // ObjectIdentity.getIdentifier() interface contract
                            String identifier = oid.getIdentifier().toString();
                            long id = Long.valueOf(identifier);

                            // Inject values
                            ps.setLong((2 * i) + 1, id);
                            ps.setString((2 * i) + 2, type);
                            i++;
                        }
                    }
                }, new ProcessResultSet(acls, sids));

        // Lookup the parents, now that our JdbcTemplate has released the database
        // connection (SEC-547)
        if (parentsToLookup.size() > 0) {
            lookupPrimaryKeys(acls, parentsToLookup, sids);
        }

        // Finally, convert our "acls" containing StubAclParents into true Acls
        Map<ObjectIdentity, Acl> resultMap = new HashMap<>();

        for (Acl inputAcl : acls.values()) {
            Assert.isInstanceOf(AclImpl.class, inputAcl,
                    "Map should have contained an AclImpl");
            Assert.isInstanceOf(Long.class, ((AclImpl) inputAcl).getId(),
                    "Acl.getId() must be Long");

            Acl result = convert(acls, (Long) ((AclImpl) inputAcl).getId());
            resultMap.put(result.getObjectIdentity(), result);
        }

        return resultMap;
    }

    /**
     * The final phase of converting the <code>Map</code> of <code>AclImpl</code>
     * instances which contain <code>StubAclParent</code>s into proper, valid
     * <code>AclImpl</code>s with correct ACL parents.
     *
     * @param inputMap        the unconverted <code>AclImpl</code>s
     * @param currentIdentity the current<code>Acl</code> that we wish to convert (this
     *                        may be
     */
    private AclImpl convert(Map<Serializable, Acl> inputMap, Long currentIdentity) {
        Assert.notEmpty(inputMap, "InputMap required");
        Assert.notNull(currentIdentity, "CurrentIdentity required");

        // Retrieve this Acl from the InputMap
        Acl uncastAcl = inputMap.get(currentIdentity);
        Assert.isInstanceOf(AclImpl.class, uncastAcl,
                "The inputMap contained a non-AclImpl");

        AclImpl inputAcl = (AclImpl) uncastAcl;

        Acl parent = inputAcl.getParentAcl();

        if ((parent != null) && parent instanceof StubAclParent) {
            // Lookup the parent
            StubAclParent stubAclParent = (StubAclParent) parent;
            parent = convert(inputMap, stubAclParent.getId());
        }

        // Now we have the parent (if there is one), create the true AclImpl
        AclImpl result = new AclImpl(inputAcl.getObjectIdentity(),
                inputAcl.getId(),
                parent, null, inputAcl.isEntriesInheriting(), inputAcl.getOwner());

        // Copy the "aces" from the input to the destination

        // Obtain the "aces" from the input ACL
        List<AccessControlEntryImpl> aces = readAces(inputAcl);

        // Create a list in which to store the "aces" for the "result" AclImpl instance
        List<AccessControlEntryImpl> acesNew = new ArrayList<>();

        // Iterate over the "aces" input and replace each nested
        // AccessControlEntryImpl.getAcl() with the new "result" AclImpl instance
        // This ensures StubAclParent instances are removed, as per SEC-951
        for (AccessControlEntryImpl ace : aces) {
            setAclOnAce(ace, result);
            acesNew.add(ace);
        }

        // Finally, now that the "aces" have been converted to have the "result" AclImpl
        // instance, modify the "result" AclImpl instance
        setAces(result, acesNew);

        return result;
    }

    /**
     * Creates a particular implementation of {@link Sid} depending on the arguments.
     *
     * @param sid         the name of the sid representing its unique identifier. In typical ACL
     *                    database schema it's located in table {@code acl_sid} table, {@code sid} column.
     * @param isPrincipal whether it's a user or granted authority like role
     * @return the instance of Sid with the {@code sidName} as an identifier
     */
    protected Sid createSid(boolean isPrincipal, String sid) {
        if (isPrincipal) {
            return new PrincipalSid(sid);
        } else {
            return new GrantedAuthoritySid(sid);
        }
    }


    public final void setPermissionFactory(PermissionFactory permissionFactory) {
        this.permissionFactory = permissionFactory;
    }

    public final void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }


    // ~ Inner Classes
    // ==================================================================================================

    private class ProcessResultSet implements ResultSetExtractor<Set<Long>> {
        private final Map<Serializable, Acl> acls;
        private final List<Sid> sids;

        public ProcessResultSet(Map<Serializable, Acl> acls, List<Sid> sids) {
            Assert.notNull(acls, "ACLs cannot be null");
            this.acls = acls;
            this.sids = sids; // can be null
        }

        /**
         * Implementation of {@link ResultSetExtractor#extractData(ResultSet)}. Creates an
         * {@link Acl} for each row in the {@link ResultSet} and ensures it is in member
         * field <tt>acls</tt>. Any {@link Acl} with a parent will have the parents id
         * returned in a set. The returned set of ids may requires further processing.
         *
         * @param rs The {@link ResultSet} to be processed
         * @return a list of parent IDs remaining to be looked up (may be empty, but never
         * <tt>null</tt>)
         * @throws SQLException
         */
        public Set<Long> extractData(ResultSet rs) throws SQLException {
            Set<Long> parentIdsToLookup = new HashSet<>(); // Set of parent_id Longs

            while (rs.next()) {
                // Convert current row into an Acl (albeit with a StubAclParent)
                convertCurrentResultIntoObject(acls, rs);

                // Figure out if this row means we need to lookup another parent
                long parentId = rs.getLong("parent_object");

                if (parentId != 0) {
                    // See if it's already in the "acls"
                    if (acls.containsKey(parentId)) {
                        continue; // skip this while iteration
                    }

                    // Now try to find it in the cache
                    MutableAcl cached = aclCache.getFromCache(parentId);

                    if ((cached == null) || !cached.isSidLoaded(sids)) {
                        parentIdsToLookup.add(parentId);
                    } else {
                        // Pop into the acls map, so our convert method doesn't
                        // need to deal with an unsynchronized AclCache
                        acls.put(cached.getId(), cached);
                    }
                }
            }

            // Return the parents left to lookup to the caller
            return parentIdsToLookup;
        }

        /**
         * Accepts the current <code>ResultSet</code> row, and converts it into an
         * <code>AclImpl</code> that contains a <code>StubAclParent</code>
         *
         * @param acls the Map we should add the converted Acl to
         * @param rs   the ResultSet focused on a current row
         * @throws SQLException if something goes wrong converting values
         */
        private void convertCurrentResultIntoObject(Map<Serializable, Acl> acls,
                                                    ResultSet rs) throws SQLException {
            Long id = rs.getLong("acl_id");

            // If we already have an ACL for this ID, just create the ACE
            Acl acl = acls.get(id);

            if (acl == null) {
                // Make an AclImpl and pop it into the Map
                ObjectIdentity objectIdentity = new ObjectIdentityImpl(
                        rs.getString("class"), rs
                        .getLong("object_id_identity"));

                Acl parentAcl = null;
                long parentAclId = rs.getLong("parent_object");

                if (parentAclId != 0) {
                    parentAcl = new StubAclParent(parentAclId);
                }

                boolean entriesInheriting = rs.getBoolean("entries_inheriting");
                Sid owner = createSid(rs.getBoolean("acl_principal"),
                        rs.getString("acl_sid"));

                acl = new AclImpl(objectIdentity, id,  parentAcl, null, entriesInheriting, owner);

                acls.put(id, acl);
            }

            // Add an extra ACE to the ACL (ORDER BY maintains the ACE list order)
            // It is permissible to have no ACEs in an ACL (which is detected by a null
            // ACE_SID)
            if (rs.getString("ace_sid") != null) {
                Long aceId = rs.getLong("ace_id");
                Sid recipient = createSid(rs.getBoolean("ace_principal"),
                        rs.getString("ace_sid"));

                int mask = rs.getInt("mask");
                Permission permission = permissionFactory.buildFromMask(mask);
                boolean granting = rs.getBoolean("granting");
                boolean auditSuccess = rs.getBoolean("audit_success");
                boolean auditFailure = rs.getBoolean("audit_failure");

                AccessControlEntryImpl ace = new AccessControlEntryImpl(aceId, acl,
                        recipient, permission, granting, auditSuccess, auditFailure);

                // Field acesField = FieldUtils.getField(AclImpl.class, "aces");
                List<AccessControlEntryImpl> aces = readAces((AclImpl) acl);

                // Add the ACE if it doesn't already exist in the ACL.aces field
                if (!aces.contains(ace)) {
                    aces.add(ace);
                }
            }
        }
    }

    private static class StubAclParent implements Acl {
        private final Long id;

        public StubAclParent(Long id) {
            this.id = id;
        }

        public List<AccessControlEntry> getEntries() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Long getId() {
            return id;
        }

        public ObjectIdentity getObjectIdentity() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Sid getOwner() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Acl getParentAcl() {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isEntriesInheriting() {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isGranted(List<Permission> permission, List<Sid> sids,
                                 boolean administrativeMode) throws NotFoundException,
                UnloadedSidException {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isSidLoaded(List<Sid> sids) {
            throw new UnsupportedOperationException("Stub only");
        }
    }


    // ~ Methods
    // ========================================================================================================

    public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity) {
        Object[] args = {parentIdentity.getIdentifier(), parentIdentity.getType()};
        String findChildrenSql = "SELECT obj.object_id_identity AS obj_id, class.class AS class "
                + "FROM acl_object_identity obj, acl_object_identity parent, acl_class class "
                + "WHERE obj.parent_object = parent.id AND obj.object_id_class = class.id "
                + "AND parent.object_id_identity = ? AND parent.object_id_class = ("
                + "SELECT id FROM acl_class WHERE acl_class.class = ?)";
        List<ObjectIdentity> objects = getJdbcTemplate().query(findChildrenSql, args,
                new RowMapper<ObjectIdentity>() {
                    public ObjectIdentity mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        String javaType = rs.getString("class");
                        Long identifier = rs.getLong("obj_id");

                        return new ObjectIdentityImpl(javaType, identifier);
                    }
                });

        if (objects.size() == 0) {
            return null;
        }

        return objects;
    }

    public Acl readAclById(ObjectIdentity object, List<Sid> sids)
            throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(Collections.singletonList(object), sids);
        Assert.isTrue(map.containsKey(object),
                "There should have been an Acl entry for ObjectIdentity " + object);

        return map.get(object);
    }

    public Acl readAclById(ObjectIdentity object) throws NotFoundException {
        return readAclById(object, null);
    }

    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects)
            throws NotFoundException {
        return readAclsById(objects, null);
    }


    // ~ Methods
    // ========================================================================================================

    public MutableAcl createAcl(ObjectIdentity objectIdentity)
            throws AlreadyExistsException {
        Assert.notNull(objectIdentity, "Object Identity required");

        // Check this object identity hasn't already been persisted
        if (retrieveObjectIdentityPrimaryKey(objectIdentity) != null) {
            throw new AlreadyExistsException("Object identity '" + objectIdentity
                    + "' already exists");
        }

        // Need to retrieve the current principal, in order to know who "owns" this ACL
        // (can be changed later on)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid sid = new PrincipalSid(auth);

        // Create the acl_object_identity row
        createObjectIdentity(objectIdentity, sid);

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval
        // etc)
        Acl acl = readAclById(objectIdentity);
        Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");

        return (MutableAcl) acl;
    }

    /**
     * Creates a new row in acl_entry for every ACE defined in the passed MutableAcl
     * object.
     *
     * @param acl containing the ACEs to insert
     */
    protected void createEntries(final MutableAcl acl) {
        if (acl.getEntries().isEmpty()) {
            return;
        }
        String insertEntry = "INSERT INTO acl_entry "
                + "(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().batchUpdate(insertEntry, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return acl.getEntries().size();
            }

            public void setValues(PreparedStatement stmt, int i) throws SQLException {
                AccessControlEntry entry_ = acl.getEntries().get(i);
                Assert.isTrue(entry_ instanceof AccessControlEntryImpl,
                        "Unknown ACE class");
                AccessControlEntryImpl entry = (AccessControlEntryImpl) entry_;

                stmt.setLong(1, (Long) acl.getId());
                stmt.setInt(2, i);
                stmt.setLong(3, createOrRetrieveSidPrimaryKey(entry.getSid(), true));
                stmt.setInt(4, entry.getPermission().getMask());
                stmt.setBoolean(5, entry.isGranting());
                stmt.setBoolean(6, entry.isAuditSuccess());
                stmt.setBoolean(7, entry.isAuditFailure());
            }
        });
    }

    /**
     * Creates an entry in the acl_object_identity table for the passed ObjectIdentity.
     * The Sid is also necessary, as acl_object_identity has defined the sid column as
     * non-null.
     *
     * @param object to represent an acl_object_identity for
     * @param owner  for the SID column (will be created if there is no acl_sid entry for
     *               this particular Sid already)
     */
    protected void createObjectIdentity(ObjectIdentity object, Sid owner) {
        Long sidId = createOrRetrieveSidPrimaryKey(owner, true);
        Long classId = createOrRetrieveClassPrimaryKey(object.getType(), true);
        String insertObjectIdentity = "INSERT INTO acl_object_identity "
                + "(object_id_class, object_id_identity, owner_sid, entries_inheriting) "
                + "VALUES (?, ?, ?, ?)";
        getJdbcTemplate().update(insertObjectIdentity, classId, object.getIdentifier(), sidId,
                Boolean.TRUE);
    }

    /**
     * Retrieves the primary key from {@code acl_class}, creating a new row if needed and
     * the {@code allowCreate} property is {@code true}.
     *
     * @param type        to find or create an entry for (often the fully-qualified class name)
     * @param allowCreate true if creation is permitted if not found
     * @return the primary key or null if not found
     */
    protected Long createOrRetrieveClassPrimaryKey(String type, boolean allowCreate) {
        String selectClassPrimaryKey = "SELECT id FROM acl_class WHERE class=?";
        List<Long> classIds = getJdbcTemplate().queryForList(selectClassPrimaryKey,
                new Object[]{type}, Long.class);

        if (!classIds.isEmpty()) {
            return classIds.get(0);
        }

        if (allowCreate) {
            String insertClass = "INSERT INTO acl_class (class) VALUES (?)";
            getJdbcTemplate().update(insertClass, type);
            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(),
                    "Transaction must be running");
            String classIdentityQuery = "CALL identity()";
            return getJdbcTemplate().queryForObject(classIdentityQuery, Long.class);
        }

        return null;
    }

    /**
     * Retrieves the primary key from acl_sid, creating a new row if needed and the
     * allowCreate property is true.
     *
     * @param sid         to find or create
     * @param allowCreate true if creation is permitted if not found
     * @return the primary key or null if not found
     * @throws IllegalArgumentException if the <tt>Sid</tt> is not a recognized
     *                                  implementation.
     */
    protected Long createOrRetrieveSidPrimaryKey(Sid sid, boolean allowCreate) {
        Assert.notNull(sid, "Sid required");

        String sidName;
        boolean sidIsPrincipal = true;

        if (sid instanceof PrincipalSid) {
            sidName = ((PrincipalSid) sid).getPrincipal();
        } else if (sid instanceof GrantedAuthoritySid) {
            sidName = ((GrantedAuthoritySid) sid).getGrantedAuthority();
            sidIsPrincipal = false;
        } else {
            throw new IllegalArgumentException("Unsupported implementation of Sid");
        }

        return createOrRetrieveSidPrimaryKey(sidName, sidIsPrincipal, allowCreate);
    }

    /**
     * Retrieves the primary key from acl_sid, creating a new row if needed and the
     * allowCreate property is true.
     *
     * @param sidName        name of Sid to find or to create
     * @param sidIsPrincipal whether it's a user or granted authority like role
     * @param allowCreate    true if creation is permitted if not found
     * @return the primary key or null if not found
     */
    protected Long createOrRetrieveSidPrimaryKey(String sidName, boolean sidIsPrincipal,
                                                 boolean allowCreate) {

        String selectSidPrimaryKey = "SELECT id FROM acl_sid WHERE principal=? AND sid=?";
        List<Long> sidIds = getJdbcTemplate().queryForList(selectSidPrimaryKey, new Object[]{
                sidIsPrincipal, sidName}, Long.class);

        if (!sidIds.isEmpty()) {
            return sidIds.get(0);
        }

        if (allowCreate) {
            String insertSid = "INSERT INTO acl_sid (principal, sid) VALUES (?, ?)";
            getJdbcTemplate().update(insertSid, Boolean.valueOf(sidIsPrincipal), sidName);
            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(),
                    "Transaction must be running");
            String sidIdentityQuery = "CALL identity()";
            return getJdbcTemplate().queryForObject(sidIdentityQuery, Long.class);
        }

        return null;
    }

    public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren)
            throws ChildrenExistException {
        Assert.notNull(objectIdentity, "Object Identity required");
        Assert.notNull(objectIdentity.getIdentifier(),
                "Object Identity doesn't provide an identifier");

        if (deleteChildren) {
            List<ObjectIdentity> children = findChildren(objectIdentity);
            if (children != null) {
                for (ObjectIdentity child : children) {
                    deleteAcl(child, true);
                }
            }
        }

        Long oidPrimaryKey = retrieveObjectIdentityPrimaryKey(objectIdentity);

        // Delete this ACL's ACEs in the acl_entry table
        deleteEntries(oidPrimaryKey);

        // Delete this ACL's acl_object_identity row
        deleteObjectIdentity(oidPrimaryKey);

        // Clear the cache
        aclCache.evictFromCache(objectIdentity);
    }

    /**
     * Deletes all ACEs defined in the acl_entry table belonging to the presented
     * ObjectIdentity primary key.
     *
     * @param oidPrimaryKey the rows in acl_entry to delete
     */
    protected void deleteEntries(Long oidPrimaryKey) {
        String deleteEntryByObjectIdentityForeignKey = "DELETE FROM acl_entry WHERE acl_object_identity=?";
        getJdbcTemplate().update(deleteEntryByObjectIdentityForeignKey, oidPrimaryKey);
    }

    /**
     * Deletes a single row from acl_object_identity that is associated with the presented
     * ObjectIdentity primary key.
     * <p>
     * We do not delete any entries from acl_class, even if no classes are using that
     * class any longer. This is a deadlock avoidance approach.
     *
     * @param oidPrimaryKey to delete the acl_object_identity
     */
    protected void deleteObjectIdentity(Long oidPrimaryKey) {
        // Delete the acl_object_identity row
        String deleteObjectIdentityByPrimaryKey = "DELETE FROM acl_object_identity WHERE id=?";
        getJdbcTemplate().update(deleteObjectIdentityByPrimaryKey, oidPrimaryKey);
    }

    /**
     * Retrieves the primary key from the acl_object_identity table for the passed
     * ObjectIdentity. Unlike some other methods in this implementation, this method will
     * NOT create a row (use {@link #createObjectIdentity(ObjectIdentity, Sid)} instead).
     *
     * @param oid to find
     * @return the object identity or null if not found
     */
    protected Long retrieveObjectIdentityPrimaryKey(ObjectIdentity oid) {
        try {
            String selectObjectIdentityPrimaryKey = "SELECT acl_object_identity.id FROM acl_object_identity, acl_class "
                    + "WHERE acl_object_identity.object_id_class = acl_class.id AND acl_class.class=? "
                    + "AND acl_object_identity.object_id_identity = ?";
            return getJdbcTemplate().queryForObject(selectObjectIdentityPrimaryKey, Long.class,
                    oid.getType(), oid.getIdentifier());
        } catch (DataAccessException notFound) {
            return null;
        }
    }

    /**
     * This implementation will simply delete all ACEs in the database and recreate them
     * on each invocation of this method. A more comprehensive implementation might use
     * dirty state checking, or more likely use ORM capabilities for create, update and
     * delete operations of {@link MutableAcl}.
     */
    public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
        Assert.notNull(acl.getId(), "Object Identity doesn't provide an identifier");

        // Delete this ACL's ACEs in the acl_entry table
        deleteEntries(retrieveObjectIdentityPrimaryKey(acl.getObjectIdentity()));

        // Create this ACL's ACEs in the acl_entry table
        createEntries(acl);

        // Change the mutable columns in acl_object_identity
        updateObjectIdentity(acl);

        // Clear the cache, including children
        clearCacheIncludingChildren(acl.getObjectIdentity());

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval
        // etc)
        return (MutableAcl) readAclById(acl.getObjectIdentity());
    }

    private void clearCacheIncludingChildren(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "ObjectIdentity required");
        List<ObjectIdentity> children = findChildren(objectIdentity);
        if (children != null) {
            for (ObjectIdentity child : children) {
                clearCacheIncludingChildren(child);
            }
        }
        aclCache.evictFromCache(objectIdentity);
    }

    /**
     * Updates an existing acl_object_identity row, with new information presented in the
     * passed MutableAcl object. Also will create an acl_sid entry if needed for the Sid
     * that owns the MutableAcl.
     *
     * @param acl to modify (a row must already exist in acl_object_identity)
     * @throws NotFoundException if the ACL could not be found to update.
     */
    protected void updateObjectIdentity(MutableAcl acl) {
        Long parentId = null;

        if (acl.getParentAcl() != null) {
            Assert.isInstanceOf(ObjectIdentityImpl.class, acl.getParentAcl()
                            .getObjectIdentity(),
                    "Implementation only supports ObjectIdentityImpl");

            ObjectIdentityImpl oii = (ObjectIdentityImpl) acl.getParentAcl()
                    .getObjectIdentity();
            parentId = retrieveObjectIdentityPrimaryKey(oii);
        }

        Assert.notNull(acl.getOwner(), "Owner is required in this implementation");

        Long ownerSid = createOrRetrieveSidPrimaryKey(acl.getOwner(), true);
        String updateObjectIdentity = "UPDATE acl_object_identity SET "
                + "parent_object = ?, owner_sid = ?, entries_inheriting = ?"
                + " WHERE id = ?";
        int count = getJdbcTemplate().update(updateObjectIdentity, parentId, ownerSid,
                Boolean.valueOf(acl.isEntriesInheriting()), acl.getId());

        if (count != 1) {
            throw new NotFoundException("Unable to locate ACL to update");
        }
    }

    public void setAclCache(AclCache aclCache) {
        this.aclCache = aclCache;
    }
}
