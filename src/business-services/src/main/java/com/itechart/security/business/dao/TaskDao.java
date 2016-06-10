package com.itechart.security.business.dao;

import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.persistent.task.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface TaskDao {

    Task get(Long id);

    List<Task> loadAll();

    Long save(Task task);

    void update(Task convert);

    void delete(Long id);

    List<Task> findTasks(TaskFilter filter);

    Task merge(Task task);

    void saveOrUpdate(Task task);
}
