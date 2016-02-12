package com.itechart.crm.security.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Cryptography {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
    private static SecretKeySpec PRIVATE_KEY = new SecretKeySpec("1234567890ABCDEF".getBytes(CHARSET), "AES");
    private static IvParameterSpec IV = new IvParameterSpec(PRIVATE_KEY.getEncoded());

    public static String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, PRIVATE_KEY, IV);
        byte[] encryptBytes = cipher.doFinal(DatatypeConverter.parseBase64Binary(message));
        String encryptMessage = DatatypeConverter.printBase64Binary(encryptBytes);

        return encryptMessage;
    }

    public static String decrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY, IV);
        byte[] decryptBytes = cipher.doFinal(DatatypeConverter.parseBase64Binary(message));
        String decryptMessage = DatatypeConverter.printBase64Binary(decryptBytes);

        return decryptMessage;
    }

    public static String createMac(String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(PRIVATE_KEY);
        byte[] macBytes = mac.doFinal(DatatypeConverter.parseBase64Binary(message));
        return DatatypeConverter.printBase64Binary(macBytes);
    }
}
