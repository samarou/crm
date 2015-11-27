package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;
import com.itechart.sample.service.dao.AclDao;
import com.itechart.sample.util.BatchExecutor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
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
                        .add(Restrictions.eq("parent.id", parentId))
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

    @Override
    public List<Acl> findByIds(List<? extends Serializable> ids) {
        List<Acl> result = new ArrayList<>(ids.size());
        BatchExecutor.execute(batch -> result.addAll(super.findByIds(batch)), ids, BATCH_SIZE);
        return result;
    }
}
