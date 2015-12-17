package com.itechart.sample.model.persistent.security;

import com.itechart.sample.model.persistent.BaseEntity;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Action allowed on business objects
 *
 * @author andrei.samarou
 */
@Entity
@Immutable
@Table(name = "action")
//todo @Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "action")
public class Action extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Override
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
}
