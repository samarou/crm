package com.itechart.security.service.impl;

import com.itechart.security.dao.GroupDao;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
