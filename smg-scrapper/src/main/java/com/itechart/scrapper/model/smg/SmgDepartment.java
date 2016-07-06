package com.itechart.scrapper.model.smg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.scrapper.model.crm.SecuredGroupDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgDepartment {
    public Integer Id;
    public String DepCode;
    public String Name;
    public Integer NumUsers;

    public SecuredGroupDto convertToCrmGroup(Long idFromCrm){
        SecuredGroupDto group = new SecuredGroupDto();
        group.setName(getDepCode());
        group.setDescription(getName());
        group.setId(idFromCrm);
        return group;
    }

    public Long getIdOfGroupFromList(List<SecuredGroupDto> list){
        for(SecuredGroupDto curGroup:list){
            if(curGroup.getName().equals(getDepCode())){
                return curGroup.getId();
            }
        }
        return null;
    }
}
