package Hash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA224 {

    String getHashFromText(String text) {
        byte[] digest = new byte[0];

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA224");
            messageDigest.update(text.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder sha224Hex = new StringBuilder(bigInt.toString(16));

        while( sha224Hex.length() < 32 ){
            sha224Hex.insert(0, "0");
        }

        return sha224Hex.toString();
    }

}
