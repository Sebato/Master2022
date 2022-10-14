import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testGUI {
    private JButton srchPatientBtn;
    private JPanel panel1;

    public testGUI() {
        srchPatientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"hello world");
            }
        });
    }

    public static void main(String[] args) {
        JFrame jframe = new JFrame("test");
        jframe.setContentPane(new testGUI().panel1);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);

    }
}
