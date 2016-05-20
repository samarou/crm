package com.itechart.security.service.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.dto.GroupDto;
import com.itechart.security.model.dto.PublicGroupDto;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.service.GroupService;

import static com.itechart.security.model.dto.Converter.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> getGroups() {
        return convertGroups(groupDao.loadAll());
    }

    @Override
    @Transactional
    public Serializable create(GroupDto group) {
        return groupDao.save(convert(group));
    }

    @Override
    @Transactional
    public void update(GroupDto group) {
        groupDao.update(convert(group));
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto get(Long id) {
        return convert(groupDao.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto getGroupWithUsers(Long id) {
        Group group = groupDao.get(id);
        return convertGroupWithUsers(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicGroupDto> getPublicGroups() {
        return convertToPublicGroups(groupDao.loadAll());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        groupDao.deleteById(id);
    }
}
