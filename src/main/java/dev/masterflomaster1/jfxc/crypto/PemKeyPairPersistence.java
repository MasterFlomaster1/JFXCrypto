package dev.masterflomaster1.jfxc.crypto;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PemKeyPairPersistence implements KeyPairPersistence {

    @Override
    public void exportPublicKey(Path target, PublicKey publicKey) {
        PemObject pemObject = new PemObject("PUBLIC KEY", publicKey.getEncoded());

        try (StringWriter stringWriter = new StringWriter(); PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
            pemWriter.flush();
            Files.writeString(target, stringWriter.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportPrivateKey(Path target, PrivateKey privateKey) {
        PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());

        try (StringWriter stringWriter = new StringWriter(); PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
            pemWriter.flush();
            Files.writeString(target, stringWriter.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PublicKey importPublicKey(Path target, String keyGenAlgorithm) {
        try {
            String pemContent = Files.readString(target, StandardCharsets.UTF_8);

            try (PemReader pemReader = new PemReader(new StringReader(pemContent))) {
                PemObject pemObject = pemReader.readPemObject();
                byte[] keyBytes = pemObject.getContent();
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(keyGenAlgorithm, "BC");
                return keyFactory.generatePublic(spec);
            }

        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrivateKey importPrivateKey(Path target, String keyGenAlgorithm) {
        try {
            String pemContent = Files.readString(target, StandardCharsets.UTF_8);

            try (PemReader pemReader = new PemReader(new StringReader(pemContent))) {
                PemObject pemObject = pemReader.readPemObject();
                byte[] keyBytes = pemObject.getContent();
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(keyGenAlgorithm, "BC");
                return keyFactory.generatePrivate(spec);
            }

        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
