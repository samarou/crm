package com.itechart.security.dao.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.persistent.Group;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class GroupDaoImpl extends BaseHibernateDao<Group> implements GroupDao {
}
