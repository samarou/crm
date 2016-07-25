package com.itechart.security.web.controller;

import com.itechart.security.business.filter.TaskFilter;
import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.PriorityService;
import com.itechart.security.business.service.StatusService;
import com.itechart.security.business.service.TaskService;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by yauheni.putsykovich on 13.06.2016.
 */

@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class TaskController extends SecuredController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private PriorityService priorityService;

    @RequestMapping(value = "/tasks", method = POST)
    public long save(@RequestBody TaskDto taskDto) {
        long id = taskService.save(taskDto);
        super.createAcl(id);
        return  id;
    }

    @RequestMapping("/tasks/{taskId}")
    public TaskDto get(@PathVariable long taskId){
        return taskService.get(taskId);
    }

    @RequestMapping(value = "/tasks/find")
    public DataPageDto<TaskDto> find(TaskFilter filter) {
        return taskService.find(filter);
    }

    @RequestMapping(value = "/tasks", method = PUT)
    public void update(@RequestBody TaskDto dto){
        taskService.update(dto);
    }

    @RequestMapping(value = "/tasks/{taskId}", method = DELETE)
    public void delete(@PathVariable long taskId){
        taskService.delete(taskId);
        super.deleteAcl(taskId);
    }

    @RequestMapping("/tasks/statuses")
    public List<NamedEntity> getStatuses(){
        return statusService.getAllStatuses();
    }

    @RequestMapping("/tasks/priorities")
    public List<NamedEntity> getPriority() {
        return priorityService.getAllPriorities();
    }

    @RequestMapping("/tasks/{taskId}/actions/{action}")
    public boolean isAllowed(@PathVariable Long taskId, @PathVariable String action) {
        return super.isAllowed(taskId, action);
    }

    @RequestMapping("/tasks/{taskId}/acls")
    @PreAuthorize("hasPermission(#taskId, 'sample.Task', 'READ')")
    public List<AclEntryDto> getAcls(@PathVariable Long taskId) {
        return super.getAcls(taskId);
    }

    @RequestMapping(value = "/tasks/{taskId}/acls", method = PUT)
    @PreAuthorize("hasPermission(#taskId, 'sample.Task', 'ADMIN')")
    public void createOrUpdateAcls(@PathVariable Long taskId, @RequestBody List<AclEntryDto> aclEntries) {
        super.createOrUpdateAcls(taskId, aclEntries);
    }

    @RequestMapping(value = "/tasks/{taskId}/acls/{principalId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#taskId, 'sample.Task', 'ADMIN')")
    public void deleteAcl(@PathVariable Long taskId, @PathVariable Long principalId) {
        super.deleteAcl(taskId, principalId);
    }

    @Override
    public ObjectTypes getObjectType() {
        return ObjectTypes.TASK;
    }
}
