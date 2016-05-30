package com.itechart.security.service.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.dto.SecuredGroupDto;
import com.itechart.security.model.dto.PublicGroupDto;
import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.GroupService;

import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static com.itechart.security.util.Converter.convertCollection;

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
        return convertCollection(groupDao.loadAll(), SecuredGroupDto::new);
    }

    @Override
    @Transactional
    public Serializable create(SecuredGroupDto group) {
        return groupDao.save(group.convert());
    }

    @Override
    @Transactional
    public void update(SecuredGroupDto group) {
        groupDao.update(group.convert());
    }

    @Override
    @Transactional(readOnly = true)
    public SecuredGroupDto get(Long id) {
        return new SecuredGroupDto(groupDao.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public SecuredGroupDto getGroupWithUsers(Long id) {
        Group group = groupDao.get(id);
        SecuredGroupDto result = new SecuredGroupDto(group);
        result.setMembers(convertCollection(group.getUsers(), PublicUserDto::new));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicGroupDto> getPublicGroups() {
        return convertCollection(groupDao.loadAll(), PublicGroupDto::new);
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
