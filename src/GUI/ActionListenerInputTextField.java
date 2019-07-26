package GUI;

import Cipher.SimpleCipher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerInputTextField implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = GUI.inputTextField.getText();
        GUI.outputTextField.setText(SimpleCipher.encryption(text));
    }
}
