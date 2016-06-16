package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.security.business.dao.TaskDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class TaskDaoImpl extends BaseHibernateDao<Task, Long, TaskFilter> implements TaskDao {
    @AclFilter(@AclFilterRule(type = Task.class, permissions = {Permission.READ}))
    public Task get(Long id) {
        return super.get(id);
    }

    @AclFilter(@AclFilterRule(type = Task.class, permissions = {Permission.READ}))
    public List<Task> find(TaskFilter filter) {
        return super.find(filter);
    }
}
