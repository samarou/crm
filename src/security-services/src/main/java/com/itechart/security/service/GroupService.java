package com.itechart.security.service;

import com.itechart.security.model.persistent.Group;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public interface GroupService {
    List<Group> fetchAll();
}
