package com.itechart.sample.service.impl;

import com.itechart.sample.service.PrivilegeService;
import com.itechart.sample.service.dao.PrivilegeDao;
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
