package com.itechart.security.service;

import com.itechart.security.model.dto.RoleDto;
import com.itechart.security.model.persistent.Role;

import java.util.List;

/**
 * Service for managing of user roles
 *
 * @author andrei.samarou
 */
public interface RoleService {

    List<RoleDto> getRoles();

    Long createRole(RoleDto role);

    void updateRole(RoleDto role);

    void deleteRole(RoleDto role);

    void deleteRoleById(Long id);

    RoleDto getRole(Long id);
}
