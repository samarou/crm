package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.PriorityDao;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convertFromPriorities;

/**
 * @author yauheni.putsykovich
 */
@Service
public class PriorityServiceImpl implements PriorityService {

    @Autowired
    private PriorityDao priorityDao;

    @Override
    @Transactional(readOnly = true)
    public List<NamedEntity> getAllPriorities() {
        return convertFromPriorities(priorityDao.loadAll());
    }

}
