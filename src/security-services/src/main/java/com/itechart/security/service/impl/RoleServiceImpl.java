package com.itechart.security.service.impl;

import com.itechart.security.model.persistent.Role;
import com.itechart.security.service.RoleService;
import com.itechart.security.dao.RoleDao;
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
