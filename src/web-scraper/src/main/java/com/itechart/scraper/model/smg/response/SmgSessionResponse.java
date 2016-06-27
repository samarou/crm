package com.itechart.scraper.model.smg.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgSessionResponse extends SmgResponse {
    public Integer SessionId;
    private static final Logger log = LoggerFactory.getLogger(SmgSessionResponse.class);

    public static Integer getSession(String userName, String password) throws IOException {
        SmgSessionResponse smgSession = getResponse(
            new URL(String.format(
                "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/Authenticate?username=%s&password=%s",
                userName, password)),
            SmgSessionResponse.class);
        log.debug("SMG sessionId: {}", smgSession.getSessionId());
        return smgSession.getSessionId();
    }
}
