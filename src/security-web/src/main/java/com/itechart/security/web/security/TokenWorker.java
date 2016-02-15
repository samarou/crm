package com.itechart.security.web.security;

import com.itechart.security.core.userdetails.UserDetailsImpl;
import com.itechart.security.web.exception.InvalidTokenException;
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
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * {@link KeyBasedPersistenceTokenService}
 *
 * Provides methods to performs
 * wrapping(create a new token from information about user) and
 * unwrapping(extracts information from token) of authentication token.
 *
 * @author yauheni.putsykovich
 */
@Component
public class TokenWorker extends KeyBasedPersistenceTokenService {
    private static final String DELIMITER = "_";

    public TokenWorker() throws NoSuchAlgorithmException {
        super.setSecureRandom(SecureRandom.getInstance("SHA1PRNG"));
        super.setServerInteger(KeyGenerators.string().hashCode());
        super.setServerSecret(KeyGenerators.string().generateKey());
    }

    public Token unwrapToken(String rawToken) throws InvalidTokenException {
        Token t;
        try {
            t = super.verifyToken(rawToken);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Verification of token failed", e);
        }
        String info = t.getExtendedInformation();
        String[] parts = info.split(DELIMITER);
        AuthToken token = new AuthToken(t.getKey(), t.getKeyCreationTime(), t.getExtendedInformation());
        token.setIp(parts[0]);
        token.setId(parts[1]);
        token.setUsername(parts[2]);
        token.setPassword(parts[3]);
        if (parts.length > 3) {
            token.setAuthorities(Arrays.stream(parts, 4, parts.length)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toCollection(HashSet::new)));
        }

        return token;
    }

    public Token wrapToken(HttpServletRequest request, Authentication authentication, String rawPassword) throws UnsupportedEncodingException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String extraInformation = new StringBuilder()
                .append(request.getRemoteAddr()).append(DELIMITER)
                .append(userDetails.getUserId()).append(DELIMITER)
                .append(userDetails.getUsername()).append(DELIMITER)
                .append(rawPassword).append(DELIMITER)
                .append(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(DELIMITER))).toString();

        return super.allocateToken(extraInformation);
    }
}
