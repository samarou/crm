package com.itechart.security.dao.impl;

import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.dao.PrivilegeDao;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class PrivilegeDaoImpl extends BaseHibernateDao<Privilege> implements PrivilegeDao {
}