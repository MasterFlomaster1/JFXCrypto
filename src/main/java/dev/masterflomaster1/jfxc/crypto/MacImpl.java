package dev.masterflomaster1.jfxc.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public final class MacImpl {

    private MacImpl() { }

    public static byte[] hmac(String algorithm, byte[] key, byte[] value) {
        try {
            Mac mac = Mac.getInstance(algorithm, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            mac.init(keySpec);
            return mac.doFinal(value);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hmac(Path input, String algorithm, byte[] key) {
        int bufferSize = 8192;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        try (FileChannel fileChannel = FileChannel.open(input, StandardOpenOption.READ)) {
            Mac mac = Mac.getInstance(algorithm, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            mac.init(keySpec);

            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                mac.update(buffer);
                buffer.clear();
            }

            return mac.doFinal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
