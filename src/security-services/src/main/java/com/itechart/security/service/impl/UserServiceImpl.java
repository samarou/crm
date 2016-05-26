package com.itechart.security.service.impl;

import com.itechart.security.dao.UserDao;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.dto.PublicUserDto;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.model.persistent.dto.Converter.convert;

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
    public PublicUserDto getPublicUser(Long userId) {
        User user = userDao.get(userId);
        return convert(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userDao.get(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithAcls(Long userId) {
        User user = userDao.get(userId);
        user.getAcls().size();
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userDao.loadAll();
    }

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
    @Transactional(readOnly = true)
    public int countUsers(UserFilter filter) {
        return userDao.countUsers(filter);
    }

    @Override
    @Transactional
    public Long createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return (Long) userDao.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        //todo: try resolve saving user by using 'update'
//        userDao.update(user);
        userDao.merge(user);
    }

    @Override
    @Transactional
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return userDao.changePassword(userName, passwordEncoder.encode(oldPassword), passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        userDao.setUserActivity(userId, true);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        userDao.setUserActivity(userId, false);
    }
}