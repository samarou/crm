package com.itechart.scraper.model.smg.response;

import com.itechart.scraper.model.smg.SmgProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgProfileResponse extends SmgResponse{
    public SmgProfile Profile;
}
