package com.itechart.security.service.impl;

import com.itechart.security.service.PrivilegeService;
import com.itechart.security.dao.PrivilegeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andrei.samarou
 */
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeDao privilegeDao;
}
