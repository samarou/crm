package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.Action;

public class ActionDto {
    private Long id;
    private String name;
    private String description;

    public ActionDto() {
    }

    public ActionDto(Action entity) {
        setId(entity.getId());
        setName(entity.getName());
        setDescription(entity.getDescription());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Action convert() {
        Action entity = new Action();
        entity.setId(getId());
        entity.setName(getName());
        entity.setDescription(getDescription());
        return entity;
    }
}
