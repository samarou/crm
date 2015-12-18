package com.itechart.security.service;

import com.itechart.security.model.persistent.Role;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface RoleService {

    List<Role> getRoles();
}
