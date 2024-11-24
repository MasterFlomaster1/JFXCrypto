package dev.masterflomaster1.jfxc.crypto.signature;

import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignatureIO {

    void sign(Path input, Path output, String algorithm, PrivateKey key);

    void verify(Path input, Path output, String algorithm, PublicKey key);

}
