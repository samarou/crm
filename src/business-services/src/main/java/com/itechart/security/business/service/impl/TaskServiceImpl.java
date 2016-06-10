package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.TaskDao;
import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.business.service.TaskService;
import com.itechart.security.model.dto.DataPageDto;
import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.itechart.security.business.model.dto.utils.TaskConverter.convertTaskDto;
import static com.itechart.security.business.model.dto.utils.TaskConverter.convertTaskMainFields;
import static com.itechart.security.core.SecurityUtils.getAuthenticatedUserId;
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
    public int count() {
        return taskDao.loadAll().size();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto get(Long id) {
        Task task = taskDao.get(id);
        PublicUserDto creator = userService.getPublicUser(task.getCreatorId());
        PublicUserDto assignee = task.getAssigneeId() != null
                ? userService.getPublicUser(task.getAssigneeId())
                : null;
        return convertTaskDto(task, creator, assignee);
    }

    @Override
    @Transactional(readOnly = true)
    public DataPageDto<TaskDto> findTasks(TaskFilter filter) {
        List<Task> tasks = taskDao.findTasks(filter);
        List<TaskDto> result = joinUsers(tasks);
        DataPageDto<TaskDto> dataPage = new DataPageDto<>();
        dataPage.setData(result);
        dataPage.setTotalCount(result.size());
        return dataPage;
    }

    private List<TaskDto> joinUsers(List<Task> tasks) {
        if (isEmpty(tasks)) {
            return emptyList();
        }
        List<Long> assigneeIds = tasks.stream().map(Task::getAssigneeId).collect(toList());
        List<Long> creatorsIds = tasks.stream().map(Task::getCreatorId).collect(toList());
        List<PublicUserDto> assigns = userService.getByIds(assigneeIds);
        List<PublicUserDto> creators = userService.getByIds(creatorsIds);
        List<TaskDto> result = new ArrayList<>(tasks.size());
        tasks.forEach(task -> {
            Optional<PublicUserDto> creator = creators.stream().filter(c -> c.getId() == task.getCreatorId()).findFirst();
            if (!creator.isPresent()) {
                throw new NullPointerException("Not found creator for task with id = " + task.getId());
            }
            Optional<PublicUserDto> assignee = assigns.stream().filter(a -> Objects.equals(a.getId(), task.getAssigneeId())).findFirst();
            result.add(convertTaskMainFields(task, creator.get(), assignee.orElse(null)));
        });
        return result;
    }

    @Override
    @Transactional
    public Long save(TaskDto dto) {
        Task task = convertTaskDto(dto);
        task.setCreatorId(getAuthenticatedUserId());
        return taskDao.save(task);
    }

    @Override
    @Transactional
    public void saveOrUpdate(TaskDto dto){
        Task task = convertTaskDto(dto);
        task.setCreatorId(getAuthenticatedUserId());
        taskDao.saveOrUpdate(task);
    }

    @Override
    @Transactional
    public void merge(TaskDto dto) {
        Task task = convertTaskDto(dto);
        task.setCreatorId(getAuthenticatedUserId());
        taskDao.merge(task);
    }

    @Override
    @Transactional
    public void update(TaskDto taskDto) {
        taskDao.update(convertTaskDto(taskDto));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskDao.delete(id);
    }
}
