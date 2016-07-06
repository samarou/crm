package com.itechart.scrapper.model.crm.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto {
    private Long id;
    private String name;
    private String description;
    private RoleDto parent;
    private Set<PrivilegeDto> privileges;

    private static RoleDto findRoleInList(String role, List<RoleDto> roles) {
        for (RoleDto aRole : roles) {
            if (!isEmpty(aRole.getName()) && aRole.getName().equals(role)) {
                return aRole;
            }
        }
        return null;
    }

    private static RoleDto getManagerFromList(List<RoleDto> roles) {
        return findRoleInList("MANAGER", roles);
    }

    private static RoleDto getSpecialistFromList(List<RoleDto> roles) {
        return findRoleInList("SPECIALIST", roles);
    }

    public static RoleDto getManagerOrSpecialistFromList(boolean isManager, List<RoleDto> roles) {
        if (isManager) {
            return getManagerFromList(roles);
        } else {
            return getSpecialistFromList(roles);
        }
    }
}
