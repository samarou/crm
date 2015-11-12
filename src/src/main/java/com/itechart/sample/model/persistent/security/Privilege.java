package com.itechart.sample.model.persistent.security;

import com.itechart.sample.model.persistent.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Privilege for doing some action with object
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "privilege")
//todo @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "privilege")
//todo use Lazy for Privilege in Role and then initialize privileges explicitly
public class Privilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "object_type_id", nullable = false)
    private ObjectType objectType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "action_id", nullable = false)
    private Action action;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
