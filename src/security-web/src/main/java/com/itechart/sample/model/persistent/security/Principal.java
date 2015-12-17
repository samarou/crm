package com.itechart.sample.model.persistent.security;

import com.itechart.sample.model.persistent.BaseEntity;

import javax.persistence.*;

/**
 * Parent class for security principals
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "principal")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Principal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract String getName();
}