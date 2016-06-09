package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.StatusDao;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.business.model.dto.utils.TaskConverter.convertFromStatuses;

/**
 * @author yauheni.putsykovich
 */
@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusDao statusDao;

    @Override
    @Transactional(readOnly = true)
    public List<NamedEntity> getAllStatuses() {
        return convertFromStatuses(statusDao.loadAll());
    }

}
