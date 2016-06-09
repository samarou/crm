package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.task.Status;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface StatusDao {

    List<Status> loadAll();

}
