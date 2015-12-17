package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.service.RoleService;
import com.itechart.sample.service.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleDao.loadAll();
    }
}
