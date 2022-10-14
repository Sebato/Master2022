package GUI_APP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class Client_GUI {
        private JButton srchPatientBtn;
        private JPanel panel1;
        private JFormattedTextField formattedTextField1;
        private JButton addPatientBtn;
        private JPanel searchPanel;
        private JTextArea txtA_Plist;

        public Client_GUI() {
            srchPatientBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(null,"hello world");
                }
            });
        }

        public static void main(String[] args) {
            JFrame jframe = new JFrame("test");
            jframe.setContentPane(new Client_GUI().panel1);
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jframe.pack();
            jframe.setVisible(true);

        }
    }
