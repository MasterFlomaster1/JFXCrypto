package Hash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA384 {

    String getHashFromText(String text) {
        byte[] digest = new byte[0];

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA384");
            messageDigest.update(text.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder sha384Hex = new StringBuilder(bigInt.toString(16));

        while( sha384Hex.length() < 32 ){
            sha384Hex.insert(0, "0");
        }

        return sha384Hex.toString();
    }

}
