import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SimpleJavaCrypter extends JFrame{
    private JButton btn1 = new JButton("Encrypt");
    private JButton btn2 = new JButton("Decrypt");
    private JLabel lbl1 = new JLabel("Enter your line here: ");
    private JLabel lbl2 = new JLabel("Result: ");
    private JTextField field = new JTextField();
    private JTextField field2 = new JTextField();

    public static void main(String[] args) {
       new SimpleJavaCrypter();
    }


    public SimpleJavaCrypter(){
        super("SimpleJavaCrypter");
        this.setSize(410, 210);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);

        lbl1.setBounds(139, 10, 132, 20);
        this.add(lbl1);

        field.setBounds(50, 40, 310, 20);
        this.add(field);

        btn1.setBounds(50, 70, 150, 30);
        btn1.addActionListener(new ActionListener1());
        this.add(btn1);

        btn2.setBounds(210, 70, 150, 30);
        btn2.addActionListener(new ActionListener2());
        this.add(btn2);

        lbl2.setBounds(184, 110, 42, 20);
        this.add(lbl2);

        field2.setBounds(50, 140, 310, 20);
        this.add(field2);

    }


    private String crypt(boolean encrypting, String line) {
        String encrypted = "";
        for (int i=0;i<line.length();i++) {
            encrypted += cryptChar(encrypting, line.charAt(i));
        }
        return encrypted;
    }

    private char cryptChar(boolean encrypt, char c) {
        String CHECKS = ("abcdefghijklmnopqrstuvwxyz1234567890");
        String REPLACES = ("0qa9zxs3wedc6vf28r5tgb7nhyujm1kiolp4");
        int index = 0;
        if (encrypt) {
            for (int i = 0; i < CHECKS.length(); i++) {
                if (c == CHECKS.charAt(i)) {
                    index = i;
                    break;
                } else {
                    index++;
                }
            }
            return REPLACES.charAt(index);
        } else {
            for (int i = 0; i < REPLACES.length(); i++) {
                if (c == REPLACES.charAt(i)) {
                    index = i;
                    break;
                } else
                    index++;
            }
            return CHECKS.charAt(index);
        }
    }

    public class ActionListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = field.getText();
            field2.setText(result(true, text));
        }
    }

    public class ActionListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String text = field.getText();
            field2.setText(result(false, text));
        }
    }


    private String result(boolean encrypt, String line){
        String word;
        if (encrypt){
            word = crypt(true, line.toLowerCase());
        } else {
            word = crypt(false, line.toLowerCase());
        }
        return word;
    }
}
