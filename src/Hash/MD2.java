package Hash;

import GUI.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MD2 {

    private MessageDigest messageDigest;

    MD2() {
        try {
            messageDigest = MessageDigest.getInstance("MD2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String getHashFromText(String text) {
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder md2Hex = new StringBuilder(bigInt.toString(16));

        while( md2Hex.length() < 32 ){
            md2Hex.insert(0, "0");
        }

        return md2Hex.toString();
    }

    String getHashFromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, read);
            };
            fis.close();
            byte[] digest = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("Error getting MD2 hash!", e.getMessage());
        }
        return null;
    }

}
