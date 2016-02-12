import com.itechart.crm.model.LoginDetails;
import com.itechart.crm.security.TokenFactory;
import com.itechart.crm.security.util.Randomizer;
import com.itechart.crm.security.util.SecureRandomizer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */
public class TokenFactoryTests {
    public static void main(String... args) throws UnknownHostException {
        Randomizer randomizer = new SecureRandomizer();
        TokenFactory tokenFactory = new TokenFactory(randomizer);
        LoginDetails loginDetails = new LoginDetails("test", "test", InetAddress.getLocalHost().getHostAddress());
        System.out.println(tokenFactory.create(loginDetails).value());
    }
}
