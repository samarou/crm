package com.itechart.security.service.impl;

import com.itechart.security.core.SecurityUtils;
import com.itechart.security.dao.UserDao;
import com.itechart.security.model.dto.*;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.Role;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.UserDefaultAclEntry;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.itechart.security.model.dto.Converter.*;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
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

    @Autowired
    private PrincipalService principalService;

    @Override
    @Transactional(readOnly = true)
    public SecuredUserDto getUser(Long userId) {
        return convert(userDao.get(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecuredUserDto> getUsers() {
        return convert(userDao.loadAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicUserDto> getPublicUsers() {
        return convertToPublicUsers(userDao.loadAll());
    }

    @Override
    @Transactional(readOnly = true)
    public User findByName(String userName) {
        return userDao.findByName(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public DataPageDto<SecuredUserDto> findUsers(SecuredUserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        DataPageDto<SecuredUserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convert(userDao.findUsers(filter)));
        dataPage.setTotalCount(userDao.countUsers(filter));
        return dataPage;
    }

    @Override
    @Transactional
    public DataPageDto<PublicUserDto> findPublicUsers(PublicUserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        filter.setActive(true);
        DataPageDto<PublicUserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convertToPublicUsers(userDao.findUsers(filter)));
        dataPage.setTotalCount(userDao.countUsers(filter));
        return dataPage;
    }

    @Override
    @Transactional
    public Long createUser(SecuredUserDto userDto) {
        User user = convert(userDto);
        user.setAcls(getDefaultAcls(user, userDto.getAcls()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return (Long) userDao.save(user);
    }

    @Override
    @Transactional
    public void updateUser(SecuredUserDto userDto) {
        //todo: try resolve saving user by using 'update'
//        userDao.update(user);
        User user = convert(userDto);
        user.setAcls(getDefaultAcls(user, userDto.getAcls()));
        userDao.merge(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDao.merge(user);
    }

    @Override
    @Transactional
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return userDao.changePassword(userName, passwordEncoder.encode(oldPassword), passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public PublicUserDto activateUser(Long userId) {
        User user = userDao.get(userId);
        if (user != null) {
            userDao.setUserActivity(userId, true);
        }
        return convertToPublicUser(user);
    }

    @Override
    @Transactional
    public PublicUserDto deactivateUser(Long userId) {
        User user = userDao.get(userId);
        if (user != null) {
                userDao.setUserActivity(userId, false);
        }
        return convertToPublicUser(user);
    }

    @Override
    @Transactional
    public void deleteAcl(Long userId, Long principalId) {
        User user = userDao.get(userId);
        user.removeDefaultAcl(principalId);
        userDao.merge(user);
    }

    @Override
    @Transactional
    public List<UserDefaultAclEntryDto> getAcls(Long userId) {
        User user = userDao.get(userId);
        return convertToDefaultAclDtos(user.getAcls());
    }

    @Override
    @Transactional
    public List<UserDefaultAclEntryDto> getDefaultAcls() {
        long userId = SecurityUtils.getAuthenticatedUserId();
        User user = userDao.get(userId);
        return convertToDefaultAclDtos(user.getAcls());
    }

    private List<UserDefaultAclEntry> getDefaultAcls(User user, List<UserDefaultAclEntryDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        List<Long> principalIds = dtos.stream().map(AclEntryDto::getPrincipalId).collect(toList());
        List<Principal> principals = principalService.getByIds(principalIds);
        return convert(user, dtos, principals);
    }
}