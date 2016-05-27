package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.UserDefaultAclEntry;

import java.util.List;

public class UserDefaultAclEntryDto extends AclEntryDto {

    private Long id;

    public UserDefaultAclEntryDto() {
    }

    public UserDefaultAclEntryDto(UserDefaultAclEntry entity) {
        super(entity.getPrincipal(), entity.getPermissions());
        setId(entity.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDefaultAclEntry convert(User user, List<Principal> principals) {
        UserDefaultAclEntry result = new UserDefaultAclEntry();
        result.setId(getId());
        result.setUser(user);
        result.setPrincipal(findPrincipalById(principals, getPrincipalId()));
        result.setPermissions(super.convert());
        return result;
    }

    private static Principal findPrincipalById(List<Principal> principals, Long id) {
        return principals.stream().
                filter((p) -> p.getId().equals(id)).
                findAny().
                orElse(null);
    }
}
