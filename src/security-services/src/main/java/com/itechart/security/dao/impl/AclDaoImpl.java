package com.itechart.security.dao.impl;

import com.itechart.security.dao.AclDao;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.model.persistent.acl.AclObjectKey;
import com.itechart.security.util.BatchExecutor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class AclDaoImpl extends BaseHibernateDao<Acl> implements AclDao {

    private static final int BATCH_SIZE = 100;

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findChildrenIds(Long parentId) {
        return getHibernateTemplate().execute(session ->
                session.createCriteria(Acl.class)
                        .add(Restrictions.eq("parentId", parentId))
                        .setProjection(Projections.property("id"))
                        .list());
    }

    @Override
    public Acl findByObjectKey(AclObjectKey key) {
        return getHibernateTemplate().execute(session ->
                (Acl) session.createCriteria(Acl.class)
                        .add(Restrictions.eq("objectKey", key))
                        .uniqueResult());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Acl> findByObjectKeys(List<AclObjectKey> keys) {
        return getHibernateTemplate().execute(session -> {
            List<Acl> result = new ArrayList<>(keys.size());
            BatchExecutor.execute(batch ->
                    result.addAll(session.createCriteria(Acl.class)
                            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                            .add(Restrictions.in("objectKey", batch))
                            .list()), keys, BATCH_SIZE);
            return result;
        });
    }
}
