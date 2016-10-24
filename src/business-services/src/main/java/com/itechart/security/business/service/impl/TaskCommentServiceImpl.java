package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.TaskCommentDao;
import com.itechart.security.business.model.dto.TaskCommentDto;
import com.itechart.security.business.model.persistent.task.TaskComment;
import com.itechart.security.business.service.TaskCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convert;

@Service
public class TaskCommentServiceImpl implements TaskCommentService{

    @Autowired
    private TaskCommentDao taskCommentDao;

    @Override
    @Transactional
    public Long saveComment(TaskCommentDto taskCommentDto) {
        TaskComment comment = convert(taskCommentDto);
        return taskCommentDao.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(TaskCommentDto taskCommentDto) {
        TaskComment comment = convert(taskCommentDto);
        taskCommentDao.update(comment);
    }

    @Override
    @Transactional
    public void delete(Long taskCommentId) {
        taskCommentDao.delete(taskCommentId);
    }
}
