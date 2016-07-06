package com.itechart.scrapper.model.smg.response;

import com.itechart.scrapper.model.smg.SmgProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgProfilesResponse extends SmgResponse {
    public List<SmgProfile> Profiles;
}
