package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.security.business.dao.TaskDao;
import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.persistent.task.Task;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class TaskDaoImpl extends BaseHibernateDao<Task, Long, TaskFilter> implements TaskDao {

    public void saveOrUpdate(Task task){
        getHibernateTemplate().saveOrUpdate(task);
    }
}
