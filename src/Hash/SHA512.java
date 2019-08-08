package Hash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA512 {

    String getHashFromText(String text) {
        byte[] digest = new byte[0];

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(text.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder sha512Hex = new StringBuilder(bigInt.toString(16));

        while( sha512Hex.length() < 32 ){
            sha512Hex.insert(0, "0");
        }

        return sha512Hex.toString();
    }

}
