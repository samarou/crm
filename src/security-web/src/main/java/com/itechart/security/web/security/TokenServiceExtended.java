package com.itechart.security.web.security;

import com.itechart.security.core.userdetails.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author yauheni.putsykovich
 */
@Component
public class TokenServiceExtended extends KeyBasedPersistenceTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceExtended.class);

    private static final String DELIMITER = "_";

    public TokenServiceExtended() throws NoSuchAlgorithmException {
        super.setSecureRandom(SecureRandom.getInstance("SHA1PRNG"));
        super.setServerInteger(KeyGenerators.string().hashCode());
        super.setServerSecret(KeyGenerators.string().generateKey());
    }

    public Token unwrapToken(String rawToken) throws Exception {
        Token t = super.verifyToken(rawToken);
        String info = t.getExtendedInformation();
        String[] parts = info.split(DELIMITER);
        TokenExtended token = new TokenExtended(t.getKey(), t.getKeyCreationTime(), t.getExtendedInformation());
        token.setIp(parts[0]);
        token.setId(parts[1]);
        token.setName(parts[2]);
        if (parts.length > 2) {
            token.setAuthorities(Arrays.stream(parts, 3, parts.length)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toCollection(HashSet::new)));
        }

        return token;
    }

    public Token wrapToken(HttpServletRequest request, Authentication authentication) throws UnsupportedEncodingException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String extraInformation = new StringBuilder()
                .append(extractUserIp(request)).append(DELIMITER)
                .append(userDetails.getUserId()).append(DELIMITER)
                .append(userDetails.getUsername()).append(DELIMITER)
                .append(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(DELIMITER))).toString();

        Token token = super.allocateToken(extraInformation);

        return token;
    }

    public String extractUserIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
