package com.itechart.security.service.impl;

import com.itechart.security.dao.RoleDao;
import com.itechart.security.model.persistent.Role;
import com.itechart.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing of user roles
 *
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

    @Override
    @Transactional
    public Long createRole(Role role) {
        return (Long) roleDao.save(role);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        roleDao.update(role);
    }

    @Override
    @Transactional
    public void deleteRole(Role role) {
        roleDao.delete(role);
    }
}
