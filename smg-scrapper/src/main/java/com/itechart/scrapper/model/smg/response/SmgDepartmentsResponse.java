package com.itechart.scrapper.model.smg.response;

import com.itechart.scrapper.model.smg.SmgDepartment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgDepartmentsResponse extends SmgResponse {
    public List<SmgDepartment> Depts;
}
