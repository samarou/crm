package com.itechart.security.business.service;

import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.model.dto.DataPageDto;

/**
 * @author yauheni.putsykovich
 */
public interface TaskService {

    long save(TaskDto taskDto);

    TaskDto get(long id);

    DataPageDto<TaskDto> find(TaskFilter filter);

    void update(TaskDto dto);

    void delete(Long id);

}
