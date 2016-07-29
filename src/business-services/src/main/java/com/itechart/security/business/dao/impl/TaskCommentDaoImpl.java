package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.TaskCommentDao;
import com.itechart.security.business.model.persistent.task.TaskComment;

import java.util.Date;


public class TaskCommentDaoImpl extends BaseHibernateDao<TaskComment, Long, PagingFilter> implements TaskCommentDao {

    @Override
    public void delete(Long id){
        TaskComment comment = getHibernateTemplate().get(TaskComment.class, id);
        if(comment != null) {
            comment.setDateDeleted(new Date());
            getHibernateTemplate().update(comment);
        }
    }

}
