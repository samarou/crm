package com.itechart.security.service.impl;

import com.itechart.security.dao.UserDao;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing of user data
 *
 * @author andrei.samarou
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findByName(String userName) {
        return userDao.findByName(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findUsers(UserFilter filter) {
        return userDao.findUsers(filter);
    }

    @Override
    @Transactional
    public Long createUser(User user) {
        return (Long) userDao.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDao.update(user);
    }

    @Override
    @Transactional
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return userDao.changePassword(userName, passwordEncoder.encode(oldPassword), passwordEncoder.encode(newPassword));
    }
}