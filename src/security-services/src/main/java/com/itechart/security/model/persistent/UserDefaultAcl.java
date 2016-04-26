package com.itechart.security.model.persistent;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author yauheni.putsykovich
 */
@Entity
@Table(name = "user_default_acl")
@PrimaryKeyJoinColumn(name = "id")
public class UserDefaultAcl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Column(name = "permission_mask")
    private int permissionMask;

    @Override
    public Serializable getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public int getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }
}
