package com.itechart.scraper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgProfile {
    public String FirstName;
    public String LastName;
    public String UserName;
    public String Email;
    public String Phone;
    public String Skype;
}