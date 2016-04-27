package com.itechart.security.web.model.dto;

/**
 * @author yauheni.putsykovich
 */
public class UserDefaultAclDto extends AclEntryDto {
    /*
    * represent user-default-acl-entry's id
    * need, because acl-entry-dto's property id represent the principal-id
    * */
    private Long recordId;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
