package com.itechart.security.model.persistent;

import com.itechart.security.core.model.SecurityGroup;

import javax.persistence.*;
import java.util.Set;

/**
 * Group of users
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "`group`")
@PrimaryKeyJoinColumn(name = "id")
public class Group extends Principal implements SecurityGroup {

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

    @Override
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
