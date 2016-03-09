package com.itechart.security.service;

import com.itechart.security.model.persistent.Principal;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public interface PrincipalService {
    List<Principal> getByIds(List<Long> ids);
}
