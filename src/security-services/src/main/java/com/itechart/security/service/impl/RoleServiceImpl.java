package com.itechart.security.service.impl;

import com.itechart.security.dao.RoleDao;
import com.itechart.security.model.dto.Converter;
import com.itechart.security.model.dto.RoleDto;
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
    public List<RoleDto> getRoles() {
        return Converter.convertRoles(roleDao.loadAll());
    }

    @Override
    @Transactional
    public Long createRole(RoleDto role) {
        return (Long) roleDao.save(Converter.convert(role));
    }

    @Override
    @Transactional
    public void updateRole(RoleDto role) {
        roleDao.update(Converter.convert(role));
    }

    @Override
    @Transactional
    public void deleteRole(RoleDto role) {
        roleDao.delete(Converter.convert(role));
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        roleDao.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDto getRole(Long id) {
        return Converter.convert(roleDao.get(id));
    }
}
