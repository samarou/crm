package com.itechart.sample.service;

import com.itechart.sample.model.persistent.security.User;

/**
 * @author andrei.samarou
 */
public interface UserService {

    User findByName(String username);
}
