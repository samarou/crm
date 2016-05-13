package com.itechart.security.web.model.dto;

/**
 * @author yauheni.putsykovich
 */
public class UserDefaultAclEntryDto extends AclEntryDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
