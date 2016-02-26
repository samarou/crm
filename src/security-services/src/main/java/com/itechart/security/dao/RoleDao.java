package com.itechart.security.dao;

import com.itechart.security.model.persistent.Role;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface RoleDao extends BaseDao<Role> {
    List getPrivilegesFor(Role role);
}
