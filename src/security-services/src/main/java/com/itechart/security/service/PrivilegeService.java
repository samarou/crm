package com.itechart.security.service;

import com.itechart.security.model.dto.PrivilegeDto;
import com.itechart.security.model.persistent.Privilege;

import java.util.List;

/**
 * Service for managing of priveleges
 *
 * @author andrei.samarou
 */
public interface PrivilegeService {

    List<PrivilegeDto> getPrivileges();
}
