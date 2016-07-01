package com.itechart.scrapper.model.smg;

import com.itechart.scrapper.model.crm.SecuredGroupDto;
import com.itechart.scrapper.model.smg.response.SmgDepartmentsResponse;
import com.itechart.scrapper.model.smg.response.SmgResponse;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Data
public class SmgDepartment {
    public Integer Id;
    public String DepCode;
    public String Name;
    public Integer NumUsers;

    public static List<SmgDepartment> getAllDepartments(Integer sessionId) throws IOException {
        URL url = new URL(String.format(
            "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/GetAllDepartments?sessionId=%s",
            sessionId));
        SmgDepartmentsResponse response = SmgResponse.getResponse(url, SmgDepartmentsResponse.class);
        return response.getDepts();
    }

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
