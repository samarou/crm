package com.itechart.security.business.model.dto.utils;

import com.itechart.security.business.model.dto.TaskDto;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.model.persistent.task.Priority;
import com.itechart.security.business.model.persistent.task.Status;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.model.dto.PublicUserDto;

import java.util.List;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by yauheni.putsykovich on 09.06.2016.
 */
public class TaskConverter {
    public static Task convertTaskDto(TaskDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setLocation(dto.getLocation());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setPriority(convertToPriority(dto.getPriority()));
        task.setStatus(convertToStatus(dto.getStatus()));
        task.setAssigneeId(dto.getAssignee() != null ? dto.getAssignee().getId() : null);
        task.setContacts(dto.getContacts().stream().map(DtoConverter::convert).collect(toList()));
        task.setCompanies(dto.getCompanies().stream().map(CompanyConverter::convert).collect(toList()));
        task.setComments(dto.getComments().stream().map(DtoConverter::convert).collect(toList()));
        return task;
    }

    public static Priority convertToPriority(NamedEntity dto) {
        Priority priority = new Priority();
        priority.setId(dto.getId());
        priority.setName(dto.getName());
        return priority;
    }

    public static Status convertToStatus(NamedEntity dto) {
        Status status = new Status();
        status.setId(dto.getId());
        status.setName(dto.getName());
        return status;
    }

    public static List<NamedEntity> convertFromStatuses(List<Status> statuses) {
        return isEmpty(statuses)
                ? emptyList()
                : statuses.stream().map(TaskConverter::convertTaskDto).collect(toList());
    }

    public static List<NamedEntity> convertFromPriorities(List<Priority> priorities) {
        return isEmpty(priorities)
                ? emptyList()
                : priorities.stream().map(TaskConverter::convertTaskDto).collect(toList());
    }

    public static NamedEntity convertTaskDto(Status status) {
        NamedEntity dto = new NamedEntity();
        dto.setId((Long) status.getId());
        dto.setName(status.getName());
        return dto;
    }

    public static NamedEntity convertTaskDto(Priority priority) {
        NamedEntity dto = new NamedEntity();
        dto.setId((Long) priority.getId());
        dto.setName(priority.getName());
        return dto;
    }

    public static TaskDto convertTaskMainFields(Task task, PublicUserDto creator, PublicUserDto assignee) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setLocation(task.getLocation());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setCreator(creator);
        dto.setAssignee(assignee);
        dto.setStatus(convertTaskDto(task.getStatus()));
        dto.setPriority(convertTaskDto(task.getPriority()));
        return dto;
    }

    public static TaskDto convertTaskDto(Task task, PublicUserDto creator, PublicUserDto assignee) {
        TaskDto dto = convertTaskMainFields(task, creator, assignee);
        dto.setCompanies(convertCollection(task.getCompanies(), CompanyConverter::convert));
        dto.setContacts(DtoConverter.convertContacts(task.getContacts()));
        dto.setComments(DtoConverter.convertTaskComments(task.getComments()));
        return dto;
    }
}
