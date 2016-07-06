package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(value = "manager", ignoreUnknown = true)
public class SecuredGroupDto extends PublicGroupDto implements ACLable {
    private List<PublicUserDto> members;
    private SecuredUserDto manager;

    @Override
    public UserDefaultAclDto convertToAcl(Boolean isAdmin) {
        UserDefaultAclDto acl = new UserDefaultAclDto();
        acl.setPrincipalId(getId());
        acl.setPrincipalTypeName("group");
        acl.setName(getName());
        acl.setCanAdmin(isAdmin);
        acl.setCanRead(!isAdmin);
        acl.setCanDelete(false);
        acl.setCanWrite(false);
        acl.setCanCreate(false);
        return acl;
    }

    public Long getIdOfGroupFromList(List<SecuredGroupDto> list) {
        for (SecuredGroupDto curGroup : list) {
            if (curGroup.getName().equals(getName())) {
                return curGroup.getId();
            }
        }
        return null;
    }
}