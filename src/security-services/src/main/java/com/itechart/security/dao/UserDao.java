package com.itechart.security.dao;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface UserDao extends BaseDao<User> {

    User findByName(String userName);

    List<User> findUsers(UserFilter filter);

    int countUsers(UserFilter filter);

    boolean changePassword(String userName, String oldPassword, String newPassword);
}