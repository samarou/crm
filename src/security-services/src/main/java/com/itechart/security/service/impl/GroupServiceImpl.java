package com.itechart.security.service.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.service.GroupService;
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
    public List<Group> getGroups() {
        return groupDao.loadAll();
    }

    @Override
    @Transactional
    public Serializable create(Group group) {
        return groupDao.save(group);
    }

    @Override
    @Transactional
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    @Transactional
    public Group get(Long id) {
        return groupDao.get(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        groupDao.deleteById(id);
    }
}
