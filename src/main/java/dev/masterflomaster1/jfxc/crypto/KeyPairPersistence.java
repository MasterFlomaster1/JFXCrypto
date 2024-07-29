package dev.masterflomaster1.jfxc.crypto;

import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairPersistence extends KeyPersistence {

    void exportPublicKey(Path target, PublicKey publicKey);
    void exportPrivateKey(Path target, PrivateKey privateKey);
    PublicKey importPublicKey(Path target, String keyGenAlgorithm);
    PrivateKey importPrivateKey(Path target, String keyGenAlgorithm);

}
