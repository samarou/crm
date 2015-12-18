package com.itechart.security.service.impl;

import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.dao.UserDao;
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
