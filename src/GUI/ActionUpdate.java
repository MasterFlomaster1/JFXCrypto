package GUI;

import Cipher.SimpleCipher;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionUpdate extends AbstractAction {
    ActionUpdate() {
        putValue(NAME, "Update cipher");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        SimpleCipher.updateCombination();
    }
}
