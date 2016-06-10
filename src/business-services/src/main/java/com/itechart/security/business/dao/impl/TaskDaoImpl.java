package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.security.business.dao.TaskDao;
import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.persistent.task.Task;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class TaskDaoImpl extends DynamicDataDaoImpl<Task, Long, TaskFilter> implements TaskDao {

    @Override
    public Task get(Long id) {
        return getHibernateTemplate().get(Task.class, id);
    }

    @Override
    public List<Task> loadAll() {
        return getHibernateTemplate().loadAll(Task.class);
    }

    @Override
    public Long save(Task task) {
        return (Long) getHibernateTemplate().save(task);
    }

    @Override
    public List<Task> findTasks(TaskFilter filter) {
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = session.createCriteria(Task.class);
            return executePagingDistinctCriteria(session, criteria, filter);
        });
    }

    @Override
    public Task merge(Task task) {
        return getHibernateTemplate().merge(task);
    }

    public void saveOrUpdate(Task task){
        getHibernateTemplate().saveOrUpdate(task);
    }
}
