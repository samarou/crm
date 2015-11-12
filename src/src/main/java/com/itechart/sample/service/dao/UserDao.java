package com.itechart.sample.service.dao;

import com.itechart.sample.model.persistent.security.User;

/**
 * @author andrei.samarou
 */
public interface UserDao extends BaseDao<User> {

    User findUser(String userName);

}
