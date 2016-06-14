package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.helpers.NamedEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */

public interface StatusService {

    List<NamedEntity> getAllStatuses();

}
