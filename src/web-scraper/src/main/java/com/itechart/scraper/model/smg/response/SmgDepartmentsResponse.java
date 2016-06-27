package com.itechart.scraper.model.smg.response;

import com.itechart.scraper.model.smg.SmgDepartment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgDepartmentsResponse extends SmgResponse {
    public List<SmgDepartment> Depts;
}
