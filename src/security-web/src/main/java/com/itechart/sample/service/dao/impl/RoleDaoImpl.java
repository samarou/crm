package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.service.dao.RoleDao;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class RoleDaoImpl extends BaseHibernateDao<Role> implements RoleDao {
}