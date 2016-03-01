package com.itechart.security.service;

import com.itechart.security.model.persistent.Group;

import java.io.Serializable;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public interface GroupService {

    List<Group> getGroups();

    Serializable create(Group group);

    void update(Group convert);

    void deleteById(Long id);
}
