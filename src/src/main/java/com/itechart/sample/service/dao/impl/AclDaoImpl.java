package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.service.dao.AclDao;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class AclDaoImpl extends BaseHibernateDao<Acl> implements AclDao {

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
    public Acl findByObjectIdentity(Serializable objectId, Long objectTypeId) {
        return getHibernateTemplate().execute(session ->
                (Acl) session.createCriteria(Acl.class)
                        .add(Restrictions.eq("objectType.id", objectTypeId))
                        .add(Restrictions.eq("objectId", objectId))
                        .uniqueResult());
    }
}
