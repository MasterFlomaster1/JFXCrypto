package dev.masterflomaster1.jfxc.core;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PemImpl {

    public static String formatKey(Key key) {
        PemObject pemObject;

        if (key instanceof PublicKey) {
            pemObject = new PemObject("PUBLIC KEY", key.getEncoded());
        } else if (key instanceof PrivateKey) {
            pemObject = new PemObject("PRIVATE KEY", key.getEncoded());
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
        }

        try (StringWriter stringWriter = new StringWriter(); PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(pemObject);
            pemWriter.flush();

            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
