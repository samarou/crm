package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.TaskDao;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.business.service.TaskService;
import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convert;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author yauheni.putsykovich
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public TaskDto get(Long id) {
        Task task = taskDao.get(id);
        PublicUserDto user = userService.getPublicUser(task.getAssignee());
        return convert(task, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> findTasks() {
        List<Task> tasks = taskDao.loadAll();
        if (isEmpty(tasks)) {
            return emptyList();
        }
        List<Long> assigneeIds = tasks.stream().map(Task::getAssignee).collect(toList());
        List<PublicUserDto> assigns = userService.getByIds(assigneeIds);
        Map<Task, PublicUserDto> taskUserMap = new HashMap<>(tasks.size());
        tasks.forEach(task -> {
            Optional<PublicUserDto> assign = assigns.stream().filter(a -> a.getId() == task.getAssignee()).findFirst();
            if (assign.isPresent()) {
                taskUserMap.put(task, assign.get());
            } else {
                throw new NullPointerException("Not found assignee for task with id = " + task.getId());
            }
        });
        return convert(taskUserMap);
    }

    @Override
    @Transactional
    public Long save(TaskDto task) {
        return taskDao.save(convert(task));
    }

    @Override
    @Transactional
    public void update(TaskDto taskDto) {
        taskDao.update(convert(taskDto));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskDao.delete(id);
    }

    @Override
    @Transactional
    public void delete(Task task) {
        taskDao.delete(task);
    }
}
