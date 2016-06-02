package com.itechart.security.service.impl;

import com.itechart.security.dao.RoleDao;
import com.itechart.security.model.dto.RoleDto;
import com.itechart.security.model.util.RoleConverter;
import com.itechart.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static com.itechart.security.model.util.RoleConverter.convert;

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
        return convertCollection(roleDao.loadAll(), RoleConverter::convert);
    }

    @Override
    @Transactional
    public Long createRole(RoleDto role) {
        return (Long) roleDao.save(convert(role));
    }

    @Override
    @Transactional
    public void updateRole(RoleDto role) {
        roleDao.update(convert(role));
    }

    @Override
    @Transactional
    public void deleteRole(RoleDto role) {
        roleDao.delete(convert(role));
    }

    @Override
    @Transactional
    public void deleteRoleById(Long id) {
        roleDao.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDto getRole(Long id) {
        return convert(roleDao.get(id));
    }
}
