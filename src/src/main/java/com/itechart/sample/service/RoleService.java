package com.itechart.sample.service;

import com.itechart.sample.model.persistent.security.Role;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface RoleService {

    List<Role> getRoles();
}
