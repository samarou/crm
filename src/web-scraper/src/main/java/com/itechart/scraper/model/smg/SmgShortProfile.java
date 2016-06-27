package com.itechart.scraper.model.smg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgShortProfile {
    public Integer ProfileId;
}
