package com.itechart.security.model.persistent;

import com.itechart.security.core.model.SecurityUser;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * User
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "user")
@PrimaryKeyJoinColumn(name = "id")
public class User extends Principal implements SecurityUser {

    @Column(name = "user_name", updatable = false, unique = true, nullable = false, length = 50)
    private String userName;

    @Column(name = "password", updatable = false, nullable = false, length = 60)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    private List<UserDefaultAclEntry> acls;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)})
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_group",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)})
    private Set<Group> groups;

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<UserDefaultAclEntry> getAcls() {
        return acls;
    }

    public void setAcls(List<UserDefaultAclEntry> acls) {
        this.acls = acls;
    }

    @Override
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public boolean removeDefaultAcl(Long principalId) {
        UserDefaultAclEntry defaultAcl = findDefaultAcl(principalId);
        return defaultAcl != null && removeDefaultAcl(defaultAcl);
    }

    private UserDefaultAclEntry findDefaultAcl(Long principalId) {
        if (!CollectionUtils.isEmpty(acls)) {
            for (UserDefaultAclEntry acl : acls) {
                if (acl.getPrincipal().getId().equals(principalId)) {
                    return acl;
                }
            }
        }
        return null;
    }

    private boolean removeDefaultAcl(UserDefaultAclEntry defaultAcl) {
        return acls.remove(defaultAcl);
    }

    public boolean leaveGroup(Group removableGroup) {
        if (groups != null) {
            Optional<Group> optionalGroup = groups.stream().filter(g -> g.equals(removableGroup)).findFirst();
            if (optionalGroup.isPresent() && groups.contains(optionalGroup.get())) {
                return groups.remove(optionalGroup.get());
            }
        }
        return false;
    }
}
