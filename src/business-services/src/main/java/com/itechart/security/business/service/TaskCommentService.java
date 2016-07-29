package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.TaskCommentDto;

public interface TaskCommentService {

    Long saveComment(TaskCommentDto taskCommentDto);

    void delete(Long taskCommentId);

}
