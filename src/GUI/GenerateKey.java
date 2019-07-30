package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import Cipher.SimpleCipher;

class GenerateKey {

    static void generateKey() {
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES:
                break;
            case CurrentCipher.AES256:
                Aes256.generateKey();
                break;
            case CurrentCipher.SimpleCipher:
                SimpleCipher.updateCombination();
                break;
        }

    }

}
