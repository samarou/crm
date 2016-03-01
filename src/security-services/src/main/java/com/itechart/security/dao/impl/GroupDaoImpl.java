package com.itechart.security.dao.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.persistent.Group;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class GroupDaoImpl extends BaseHibernateDao<Group> implements GroupDao {
    @Override
    public void deleteById(Long id) {
        getHibernateTemplate().executeWithNativeSession(session ->
                session.createSQLQuery("delete from `group` where id = :id")
                       .setLong("id", id)
                       .executeUpdate());
    }
}
