import com.itechart.crm.security.util.Cryptography;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 *
 */
public class CryptographyTests {
    @Test
    public void encrypt_weMustReceivedSourceMessage() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        String message = "AAAAbbbbCCCCdddd";
        String encrypt = Cryptography.encrypt(message);
        System.out.println("encrypt: " + encrypt);
        assertNotNull(encrypt);
        String decrypt = Cryptography.decrypt(encrypt);
        System.out.println("decrypt: " + decrypt);
        assertNotNull(decrypt);
        assertEquals(message, decrypt);
    }

    @Test
    public void messageLength_passMessageWithLengthNotMultiple16_mustNotPass() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        String message = "AAAAbbbbCCCCdddd___";
        String encrypt = Cryptography.encrypt(message);
        System.out.println("encrypt: " + encrypt);
        assertNotNull(encrypt);
        String decrypt = Cryptography.decrypt(encrypt);
        System.out.println("decrypt: " + decrypt);
        assertNotNull(decrypt);
        assertEquals(message, decrypt);
    }

    @Test
    public void mac_TwoMacsMustBeNotEquals() throws InvalidKeyException, NoSuchAlgorithmException {
        String message1 = "message1";
        String message2 = "message2";
        String mac1 = Cryptography.createMac(message1);
        String mac2 = Cryptography.createMac(message2);
        assertNotEquals(mac1, mac2);
    }
}
