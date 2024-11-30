package dev.masterflomaster1.jfxc.core.io;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CipherNIO implements CipherIO {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public void encrypt(Cipher cipher, Path target, Path destination) {
        nio(cipher, target, destination);
    }

    @Override
    public void decrypt(Cipher cipher, Path target, Path destination) {
        nio(cipher, target, destination);
    }

    /**
     * Encrypts or decrypts the content of a file and writes the result to a destination file using NIO boosting
     * operation speed.
     *
     * @param cipher The Cipher instance already initialized for encryption or decryption.
     * @param target The path to the source file to process.
     * @param destination The path to the destination file where processed data will be written.
     */
    private void nio(Cipher cipher, Path target, Path destination) {
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
