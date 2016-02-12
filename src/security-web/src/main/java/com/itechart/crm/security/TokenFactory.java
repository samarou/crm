package com.itechart.crm.security;

import com.itechart.crm.model.LoginDetails;
import com.itechart.crm.security.util.Cryptography;
import com.itechart.crm.security.util.Randomizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class TokenFactory {
    public static class Token {
        private String token;

        public Token(String token) {
            this.token = token;
        }

        public String value() {
            return this.token;
        }
    }

    private Randomizer randomizer;

    public TokenFactory(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    public Token create(LoginDetails login) {
        try {
            long userId = randomizer.next();
            long time = System.currentTimeMillis();
            String rawTokenString = userId + "_" + time + "_" + login.getIp() + "_" + login.getUsername() + "$" + Cryptography.createMac(login.getPassword());
            String tokenString = Cryptography.encrypt(rawTokenString);
            return new Token(tokenString);
        } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Creation token failed");
        }
    }
}
