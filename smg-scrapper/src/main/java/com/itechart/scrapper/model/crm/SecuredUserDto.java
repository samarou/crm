package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.scrapper.model.crm.roles.RoleDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

import static com.itechart.scrapper.ScrapperUtils.addElementsToSet;
import static org.springframework.util.StringUtils.isEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecuredUserDto extends PublicUserDto implements ACLable {
    private String password;
    private Boolean active;
    private Set<SecuredGroupDto> groups;
    private Set<UserDefaultAclDto> acls;
    private Set<RoleDto> roles;

    public void addFields(SecuredUserDto another) {
        super.addFields(another);
        setPassword(!isEmpty(getPassword()) ? getPassword() : another.getPassword());
        setActive(!isEmpty(getActive()) ? getActive() : another.getActive());
        addElementsToSet(groups, another.getGroups());
        addElementsToSet(acls, another.getAcls());
        addElementsToSet(roles, another.getRoles());
    }

    @Override
    public UserDefaultAclDto convertToAcl(Boolean isAdmin) {
        UserDefaultAclDto acl = new UserDefaultAclDto();
        acl.setPrincipalId(getId());
        acl.setPrincipalTypeName("user");
        acl.setName(getUserName());
        acl.setCanAdmin(isAdmin);
        acl.setCanRead(!isAdmin);
        acl.setCanDelete(false);
        acl.setCanWrite(false);
        acl.setCanCreate(false);
        return acl;
    }
}
