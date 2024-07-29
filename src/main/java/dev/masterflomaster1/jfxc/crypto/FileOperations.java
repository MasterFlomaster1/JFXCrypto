package dev.masterflomaster1.jfxc.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Utility class for file encryption and decryption using NIO.
 */
public final class FileOperations {

    private static final int BUFFER_SIZE = 8192;

    private FileOperations() { }

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
    public static void encrypt(Cipher cipher, Path target, Path destination) {
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
    public static void decrypt(Cipher cipher, Path target, Path destination) {
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

    /**
     * Encrypts or decrypts the content of a file and writes the result to a destination file using NIO boosting
     * operation speed.
     *
     * @param cipher The Cipher instance already initialized for encryption or decryption.
     * @param target The path to the source file to process.
     * @param destination The path to the destination file where processed data will be written.
     */
    public static void nioEncryptAndDecrypt(Cipher cipher, Path target, Path destination) {
        try (FileChannel sourceChannel = FileChannel.open(target, StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(destination,
                     StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            ByteBuffer encryptedBuffer;

            while (sourceChannel.read(buffer) != -1) {
                buffer.flip();
                encryptedBuffer = ByteBuffer.wrap(cipher.update(buffer.array(), buffer.position(), buffer.remaining()));
                destChannel.write(encryptedBuffer);
                buffer.clear();
            }

            encryptedBuffer = ByteBuffer.wrap(cipher.doFinal());
            destChannel.write(encryptedBuffer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
