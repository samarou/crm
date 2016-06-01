package com.itechart.security.web.security.token;

import com.itechart.security.web.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * Service performs generating of new security tokens,
 * validating and extracting data from user security tokens
 *
 * @author andrei.samarou
 */
@Service
//todo move to jwt
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final String HEADER_AUTH_TOKEN = "X-Auth-Token";

    private KeyBasedPersistenceTokenService tokenHelper;

    private Long tokenTimeToLive;

    public TokenService() throws NoSuchAlgorithmException {
        StringKeyGenerator keyGenerator = KeyGenerators.string();
        tokenHelper = new KeyBasedPersistenceTokenService();
        tokenHelper.setServerInteger(keyGenerator.hashCode());
        tokenHelper.setServerSecret(keyGenerator.generateKey());
        tokenHelper.setSecureRandom(SecureRandom.getInstance("SHA1PRNG"));
        tokenHelper.setPseudoRandomNumberBytes(8);
    }

    public TokenData parseToken(String rawToken) throws InvalidTokenException {
        Token token;
        try {
            token = tokenHelper.verifyToken(rawToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Token verification was failed", e);
        }
        long creationTime = token.getKeyCreationTime();
        if (tokenTimeToLive != null && tokenTimeToLive < System.currentTimeMillis() - creationTime) {
            throw new InvalidTokenException("Expired token");
        }
        String extraInfo = token.getExtendedInformation();
        String[] parts = extraInfo.split(":");
        if (parts.length != 2) {
            throw new InvalidTokenException("Invalid token");
        }
        TokenData tokenData = new TokenData();
        tokenData.setUsername(parts[0]);
        String remoteAddrEncoded = parts[1];
        if (!StringUtils.isEmpty(remoteAddrEncoded)) {
            try {
                byte[] remoteAddressBytes = Base64.getDecoder().decode(remoteAddrEncoded);
                tokenData.setRemoteAddr(InetAddress.getByAddress(remoteAddressBytes).getHostAddress());
            } catch (Exception e) {
                throw new InvalidTokenException("Invalid token data", e);
            }
        }
        return tokenData;
    }

    public String generateToken(TokenData tokenData) {
        StringBuilder extraInformation = new StringBuilder(tokenData.getUsername()).append(":");
        if (!StringUtils.isEmpty(tokenData.getRemoteAddr())) {
            try {
                InetAddress inetAddress = InetAddress.getByName(tokenData.getRemoteAddr());
                extraInformation.append(Base64.getEncoder().encodeToString(inetAddress.getAddress()));
            } catch (Exception e) {
                logger.error("Can't parse user remote address", e);
            }
        } else {
            logger.warn("User remote address is empty");
        }
        Token token = tokenHelper.allocateToken(extraInformation.toString());
        return token.getKey();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            Optional<Cookie> optionalCookie = Arrays.asList(cookies)
                    .stream()
                    .filter(cookie -> cookie.getName().equals(HEADER_AUTH_TOKEN))
                    .findFirst();
            if (optionalCookie.isPresent()) {
                token =  optionalCookie.get().getValue();
            }
        }
        return token;
    }

    public void setTokenToResponse(HttpServletResponse response, String token) {
        Cookie cookie = createCookie(HEADER_AUTH_TOKEN, token, Math.toIntExact(tokenTimeToLive / 1000));
        response.addCookie(cookie);
    }

    public void deleteTokenFromResponse(HttpServletResponse response) {
        Cookie cookie = createCookie(HEADER_AUTH_TOKEN, null, 0);
        response.addCookie(cookie);
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    public void setTokenTimeToLive(Long tokenTimeToLive) {
        this.tokenTimeToLive = tokenTimeToLive;
    }
}