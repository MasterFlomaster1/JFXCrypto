package dev.masterflomaster1.jfxc.core.io;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

public class CipherBlockingIO implements CipherIO {

    /**
     * Encrypts the content of a file and writes the encrypted data to a destination file.
     *
     * <p>This method uses traditional IO operations, which may be less efficient compared to NIO. Ensure that the
     * {@code Cipher} instance is properly initialized for decryption before calling this method.</p>
     *
     * @param cipher The {@link Cipher} instance initialized for decryption.
     * @param target The path to the source file to decrypt.
     * @param destination The path to the destination file where decrypted data will be written.
     */
    @Override
    public void encrypt(Cipher cipher, Path target, Path destination) {
        try (FileInputStream fis = new FileInputStream(target.toString());
             FileOutputStream fos = new FileOutputStream(destination.toString());
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypts the content of a file and writes the decrypted data to a destination file using traditional IO.
     *
     * <p>This method uses traditional IO operations, which may be less efficient compared to NIO. Ensure that the
     * {@code Cipher} instance is properly initialized for decryption before calling this method.</p>
     *
     * @param cipher The {@link Cipher} instance initialized for decryption.
     * @param target The path to the source file to decrypt.
     * @param destination The path to the destination file where decrypted data will be written.
     */
    @Override
    public void decrypt(Cipher cipher, Path target, Path destination) {
        try (FileInputStream fis = new FileInputStream(target.toString());
             FileOutputStream fos = new FileOutputStream(destination.toString());
             CipherInputStream cis = new CipherInputStream(fis, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
