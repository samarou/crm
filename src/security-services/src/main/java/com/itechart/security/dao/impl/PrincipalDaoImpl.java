package com.itechart.security.dao.impl;

import com.itechart.security.dao.PrincipalDao;
import com.itechart.security.model.persistent.Principal;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class PrincipalDaoImpl extends BaseHibernateDao<Principal> implements PrincipalDao {
}
