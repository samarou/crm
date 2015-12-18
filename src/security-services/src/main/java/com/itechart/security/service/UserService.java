package com.itechart.security.service;

import com.itechart.security.model.persistent.User;

/**
 * @author andrei.samarou
 */
public interface UserService {

    User findByName(String username);
}
