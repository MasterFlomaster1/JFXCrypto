package Hash;

import GUI.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA224 {

    private MessageDigest messageDigest;

    SHA224() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String getHashFromText(String text) {
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder sha224Hex = new StringBuilder(bigInt.toString(16));

        while( sha224Hex.length() < 32 ){
            sha224Hex.insert(0, "0");
        }

        return sha224Hex.toString();
    }

    String getHashFromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, read);
            }
            fis.close();
            byte[] digest = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("Error getting SHA224 hash!", e.getMessage());
        }
        return null;
    }

}
