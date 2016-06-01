package com.itechart.security.service.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.dto.SecuredGroupDto;
import com.itechart.security.model.dto.PublicGroupDto;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.util.GroupConverter;
import com.itechart.security.model.util.UserConverter;
import com.itechart.security.service.GroupService;

import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static com.itechart.security.model.util.GroupConverter.convert;
import static com.itechart.security.model.util.GroupConverter.convertToSecuredDto;

/**
 * @author yauheni.putsykovich
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<SecuredGroupDto> getGroups() {
        return convertCollection(groupDao.loadAll(), GroupConverter::convertToSecuredDto);
    }

    @Override
    @Transactional
    public Serializable create(SecuredGroupDto group) {
        return groupDao.save(convert(group));
    }

    @Override
    @Transactional
    public void update(SecuredGroupDto group) {
        groupDao.update(convert(group));
    }

    @Override
    @Transactional(readOnly = true)
    public SecuredGroupDto get(Long id) {
        return convertToSecuredDto(groupDao.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public SecuredGroupDto getGroupWithUsers(Long id) {
        Group group = groupDao.get(id);
        SecuredGroupDto result = convertToSecuredDto(group);
        result.setMembers(convertCollection(group.getUsers(), UserConverter::convertToPublicDto));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicGroupDto> getPublicGroups() {
        return convertCollection(groupDao.loadAll(), GroupConverter::convertToPublicDto);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Group group = groupDao.get(id);
        Set<User> users = group.getUsers();
        users.forEach(user -> {
            user.removeFromGroup(group);
            userService.updateUser(user);
        });
        groupDao.deleteById(id);
    }
}
