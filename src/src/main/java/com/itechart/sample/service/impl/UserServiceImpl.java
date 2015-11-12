package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.service.UserService;
import com.itechart.sample.service.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andrei.samarou
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public User findByName(String username) {
        return userDao.findUser(username);
    }
}
