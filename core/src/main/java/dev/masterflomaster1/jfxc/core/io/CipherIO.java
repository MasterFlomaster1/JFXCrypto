package dev.masterflomaster1.jfxc.core.io;

import javax.crypto.Cipher;
import java.nio.file.Path;

public interface CipherIO {

    void encrypt(Cipher cipher, Path target, Path destination);
    void decrypt(Cipher cipher, Path target, Path destination);

}
