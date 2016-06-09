package com.itechart.security.business.model.persistent.task;

import com.itechart.security.business.model.persistent.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Entity
@Getter
@Setter
@Table(name = "status")
public class Status extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @OneToMany(mappedBy = "status")
    private List<Task> tasks;

    @Override
    public Serializable getId() {
        return id;
    }
}
