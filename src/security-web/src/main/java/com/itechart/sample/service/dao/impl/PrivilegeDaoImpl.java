package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.Privilege;
import com.itechart.sample.service.dao.PrivilegeDao;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class PrivilegeDaoImpl extends BaseHibernateDao<Privilege> implements PrivilegeDao {
}