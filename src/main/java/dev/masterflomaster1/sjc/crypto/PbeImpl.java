package dev.masterflomaster1.sjc.crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.concurrent.CompletableFuture;

public final class PbeImpl {

    private PbeImpl() { }

    public static CompletableFuture<byte[]> asyncHash(String algorithm, char[] password, byte[] salt, int iter, int kLen) {
        return CompletableFuture.supplyAsync(() -> {
            KeySpec spec = new PBEKeySpec(password, salt, iter, kLen);

            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm, "BC");
                return factory.generateSecret(spec).getEncoded();
            } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
