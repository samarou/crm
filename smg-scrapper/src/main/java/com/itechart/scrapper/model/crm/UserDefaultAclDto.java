package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDefaultAclDto {
    private Long id;
    private Long principalId;
    private String name;
    private String principalTypeName;
    private Boolean canRead;
    private Boolean canWrite;
    private Boolean canCreate;
    private Boolean canDelete;
    private Boolean canAdmin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDefaultAclDto)) return false;

        UserDefaultAclDto that = (UserDefaultAclDto) o;

        return getPrincipalId() != null ? getPrincipalId().equals(that.getPrincipalId()) : that.getPrincipalId() == null
            && (getName() != null ? getName().equals(that.getName()) : that.getName() == null
            && (getPrincipalTypeName() != null ? getPrincipalTypeName().equals(that.getPrincipalTypeName()) : that.getPrincipalTypeName() == null));

    }

    @Override
    public int hashCode() {
        int result = getPrincipalId() != null ? getPrincipalId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPrincipalTypeName() != null ? getPrincipalTypeName().hashCode() : 0);
        return result;
    }
}
