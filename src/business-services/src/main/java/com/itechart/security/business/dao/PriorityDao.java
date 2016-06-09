package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.task.Priority;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface PriorityDao {

    List<Priority> loadAll();

}
