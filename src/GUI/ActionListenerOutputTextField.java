package GUI;

import Cipher.SimpleCipher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerOutputTextField implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = GUI.inputTextField.getText();
        GUI.outputTextField.setText(SimpleCipher.decryption(text));
    }
}
