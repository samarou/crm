package com.itechart.security.business.service;

import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.model.dto.DataPageDto;
import org.springframework.stereotype.Service;

/**
 * @author yauheni.putsykovich
 */
@Service
public interface TaskService {

    int count();

    TaskDto get(Long id);

    DataPageDto<TaskDto> findTasks(TaskFilter filter);

    Long save(TaskDto task);

    void update(TaskDto taskDto);

    void saveOrUpdate(TaskDto dto);

    void merge(TaskDto dto);

    void delete(Long id);
}
