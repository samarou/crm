package com.itechart.sample.model.persistent.security;

import com.itechart.sample.model.persistent.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Group of users
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "`group`")
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
