package Hash;

import GUI.Main.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA1 {

    private MessageDigest messageDigest;

    SHA1() {
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String getHashFromText(String text) {
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder sha1Hex = new StringBuilder(bigInt.toString(16));

        while( sha1Hex.length() < 32 ){
            sha1Hex.insert(0, "0");
        }

        return sha1Hex.toString();
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
            AlertDialog.showError("Error getting SHA1 hash!", e.getMessage());
        }
        return null;
    }

}
