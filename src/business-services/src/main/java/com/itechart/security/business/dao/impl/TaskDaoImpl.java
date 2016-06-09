package com.itechart.security.business.dao.impl;

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
public class TaskDaoImpl extends AbstractHibernateDao<Task> implements TaskDao {

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
    public void update(Task convert) {
        getHibernateTemplate().update(convert);
    }

    @Override
    public boolean delete(Long id) {
        return super.deleteById(id);
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
