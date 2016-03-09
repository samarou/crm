package com.itechart.security.service.impl;

import com.itechart.security.dao.PrincipalDao;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Service
public class PrincipalServiceImpl implements PrincipalService {

    @Autowired
    private PrincipalDao principalDao;

    @Override
    public List<Principal> getByIds(List<Long> ids) {
        return principalDao.findByIds(ids);
    }
}
