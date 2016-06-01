package com.itechart.security.web.controller;

import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.service.PriorityService;
import com.itechart.security.business.service.StatusService;
import com.itechart.security.business.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private PriorityService priorityService;

    @RequestMapping(value = "/tasks/{id}")
    private TaskDto get(@PathVariable Long id){
        return taskService.get(id);
    }

    @RequestMapping(value = "/tasks", method = GET)
    private List<TaskDto> findTasks(){
        return taskService.findTasks();
    }

    @RequestMapping(value = "/tasks", method = POST)
    private Long save(@RequestBody TaskDto task) {
        return taskService.save(task);
    }

    @RequestMapping(value = "/tasks", method = PUT)
    private void update(@RequestBody TaskDto taskDto){
        taskService.update(taskDto);
    }

    @RequestMapping(value = "/tasks/{id}", method = DELETE)
    private void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @RequestMapping(value = "/tasks/statuses")
    private List<NamedEntity> getStatuses(){
        return statusService.getAllStatuses();
    }

    @RequestMapping(value = "/tasks/priorities")
    private List<NamedEntity> getPriorities(){
        return priorityService.getAllPriorities();
    }

}
