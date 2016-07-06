package com.itechart.scrapper.model.smg.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgSessionResponse extends SmgResponse {
    public Integer SessionId;
}
