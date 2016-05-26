package com.itechart.security.service;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.dto.PublicUserDto;

import java.util.List;

/**
 * Service for managing of user data
 *
 * @author andrei.samarou
 */
public interface UserService {
    User getUser(Long userId);

    User getUserWithAcls(Long userId);

    List<User> getUsers();

    User findByName(String userName);

    List<User> findUsers(UserFilter filter);

    int countUsers(UserFilter filter);

    Long createUser(User user);

    void updateUser(User user);

    boolean changePassword(String userName, String oldPassword, String newPassword);

    void activateUser(Long userId);

    void deactivateUser(Long userId);

    PublicUserDto getPublicUser(Long userId);
}