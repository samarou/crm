package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.persistent.task.Task;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface TaskDao extends BaseDao<Task, Long, TaskFilter> {

    void saveOrUpdate(Task task);
}
