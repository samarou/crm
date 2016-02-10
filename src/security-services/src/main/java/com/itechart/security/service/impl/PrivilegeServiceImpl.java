package com.itechart.security.service.impl;

import com.itechart.security.dao.PrivilegeDao;
import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing of priveleges
 *
 * @author andrei.samarou
 */
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Override
    @Transactional(readOnly = true)
    public List<Privilege> getPrivileges() {
        return privilegeDao.loadAll();
    }
}
