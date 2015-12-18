package com.itechart.security.dao;

import com.itechart.security.model.persistent.User;

/**
 * @author andrei.samarou
 */
public interface UserDao extends BaseDao<User> {

    User findUser(String userName);

}
