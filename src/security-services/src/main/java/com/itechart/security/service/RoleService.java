package com.itechart.security.service;

import com.itechart.security.model.persistent.Role;
import com.itechart.security.model.persistent.User;

import java.util.List;

/**
 * Service for managing of user roles
 *
 * @author andrei.samarou
 */
public interface RoleService {

    List<Role> getRoles();

    Long createRole(Role role);

    void updateRole(Role role);

    void deleteRole(Role role);
}
