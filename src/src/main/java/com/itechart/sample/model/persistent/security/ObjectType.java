package com.itechart.sample.model.persistent.security;

import com.itechart.sample.model.persistent.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Type of business object
 *
 * @author andrei.samarou
 */
@Entity
@Immutable
@Table(name = "object_type")
//todo @Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "objectType")
public class ObjectType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
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
