package com.itechart.security.service.impl;

import com.itechart.security.dao.PrivilegeDao;
import com.itechart.security.model.dto.PrivilegeDto;
import com.itechart.security.model.util.PrivilegeConverter;
import com.itechart.security.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;

/**
 * Service for managing of priveleges
 *
 * @author andrei.samarou
 */
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Override
    @Transactional(readOnly = true)
    public List<PrivilegeDto> getPrivileges() {
        return convertCollection(privilegeDao.loadAll(), PrivilegeConverter::convert);
    }
}
