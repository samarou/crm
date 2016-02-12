package com.itechart.security.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author yauheni.putsykovich
 */
@Component
public class Tokens {
    private String password;
    private String salt;
    private BytesEncryptor encryptor;

    public Tokens() {
        this(KeyGenerators.string().generateKey(), KeyGenerators.string().generateKey());
    }

    public Tokens(String password, String salt) {
        this.password = password;
        this.salt = salt;
        this.encryptor = Encryptors.stronger(password, salt);
    }

    public String unwrapToken(String rawToken) {
        String token = new String(encryptor.decrypt(Base64.decode(rawToken.getBytes(UTF_8))), UTF_8);
        System.out.println(token);
        int start = 0;
        String id = token.substring(0, token.indexOf('_'));
        start += id.length() + 1;
        String time = token.substring(start, token.indexOf('_', start));
        start += time.length() + 1;
        String ip = token.substring(start, token.indexOf('$', start));
        start += ip.length() + 1;
        String name = token.substring(start);

        System.out.printf("encrypt: %s, decrypt: %s\nid=%s,\ntime=%s\nip=%s\nname=%s\n", rawToken, token, id, time, ip, name);

        return null;
    }

    public String wrapToken(HttpServletRequest request, Authentication authentication) throws UnsupportedEncodingException {
        //todo: implements fetching data from arguments
        long id = 1L;
        long time = System.currentTimeMillis();
        String ip = "192.168.0.30";
        String name = "user";

        String rawToken = String.format("%s_%s_%s$%s", id, time, ip, name);
        byte[] result = encryptor.encrypt(rawToken.getBytes(UTF_8));
        String encrypt = new String(Base64.encode(result), UTF_8);
        System.out.println("source: " + rawToken + ", encrypt: " + encrypt);
        return encrypt;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
