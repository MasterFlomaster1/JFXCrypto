package GUI;

import Util.SystemInfo;
import Cipher.SimpleCipher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private JTextField inputTextField = new JTextField();
    private JTextField outputTextField = new JTextField();

    public GUI() {
        SystemInfo.operatingSystem();
        setLookAndFeel();
        this.setTitle("SimpleJavaCrypter");

        this.setSize(410, 240);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem update = new JMenuItem(new Update());
        try {
            update.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/update.jpg"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.add(update);
        menuBar.add(file);
        this.setJMenuBar(menuBar);

        JLabel lbl1 = new JLabel("Enter your line here: ");
        lbl1.setBounds(155, 10, 132, 20);
        this.add(lbl1);

        inputTextField.setBounds(50, 40, 310, 20);
        this.add(inputTextField);

        JButton btn1 = new JButton("Encrypt");
        btn1.setBounds(50, 70, 150, 30);
        btn1.addActionListener(new GUI.ActionListener1());
        this.add(btn1);

        JButton btn2 = new JButton("Decrypt");
        btn2.setBounds(210, 70, 150, 30);
        btn2.addActionListener(new GUI.ActionListener2());
        this.add(btn2);

        JLabel lbl2 = new JLabel("Result: ");
        lbl2.setBounds(184, 110, 42, 20);
        this.add(lbl2);

        outputTextField.setBounds(50, 140, 310, 20);
        this.add(outputTextField);

        this.setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            if (SystemInfo.isWindows) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (SystemInfo.isLinux) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else if (SystemInfo.isMacOs) {
                //Unchecked
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ActionListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = inputTextField.getText();
            outputTextField.setText(result(true, text));
        }
    }

    public class ActionListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = inputTextField.getText();
            outputTextField.setText(result(false, text));
        }
    }

    static class Update extends AbstractAction {
        Update() {
            putValue(NAME, "Update combination");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleCipher.updateCombination();
        }
    }

    private String result(boolean encrypt, String line){
        String word;
        if (encrypt){
            word = SimpleCipher.crypt(true, line);
        } else {
            word = SimpleCipher.crypt(false, line);
        }
        return word;
    }

}
