package com.itechart.security.service;

import com.itechart.security.model.dto.*;
import com.itechart.security.model.persistent.User;

import java.util.List;

/**
 * Service for managing of user data
 *
 * @author andrei.samarou
 */
public interface UserService {

    SecuredUserDto getUser(Long userId);

//    User getUserWithAcls(Long userId);

    public void deleteAcl(Long userId, Long principalId);

    List<UserDefaultAclEntryDto> getAcls(Long userId);

    List<SecuredUserDto> getUsers();

    List<PublicUserDto> getPublicUsers();

    User findByName(String userName);

    DataPageDto<SecuredUserDto> findUsers(SecuredUserFilterDto filter);

    DataPageDto<PublicUserDto> findPublicUsers(PublicUserFilterDto filter);

    Long createUser(SecuredUserDto user);

    void updateUser(SecuredUserDto user);

    boolean changePassword(String userName, String oldPassword, String newPassword);

    PublicUserDto activateUser(Long userId);

    PublicUserDto deactivateUser(Long userId);

    List<UserDefaultAclEntryDto> getDefaultAcls();
}