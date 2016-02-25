package com.itechart.security.dao.impl;

import com.itechart.security.dao.RoleDao;
import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.model.persistent.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class RoleDaoImpl extends BaseHibernateDao<Role> implements RoleDao {
    @Override
    public List<Privilege> getPrivilegesFor(Role role) {
        return getHibernateTemplate().executeWithNativeSession(session -> session
                .createQuery("select r.privileges from Role r where r.id = :id")
                .setLong("id", role.getId())
                .list());
    }
}