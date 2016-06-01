package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.persistent.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Service
public interface TaskService {

    TaskDto get(Long id);

    List<TaskDto> findTasks();

    Long save(TaskDto task);

    void update(TaskDto taskDto);

    void delete(Long id);

    void delete(Task task);
}
