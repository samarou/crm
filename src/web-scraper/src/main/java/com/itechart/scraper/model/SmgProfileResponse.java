package com.itechart.scraper.model;

import lombok.Data;

@Data
public class SmgProfileResponse {
    public String ErrorCode;
    public String Permission;
    public SmgProfile Profile;
}
