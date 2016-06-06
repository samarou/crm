package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.dao.DynamicDataDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;

/**
 * @author andrei.samarou
 */
public interface ContactDao extends BaseDao<Contact, Long, ContactFilter>, DynamicDataDao<Contact, Long> {

    void deleteSkill(Long id);

}
