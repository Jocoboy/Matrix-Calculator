import javax.swing.*;
import java.awt.*;

public class About extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel JP;

    public About() {
        setTitle("About");
        setSize(500, 400);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JP = new JPanel();
        JP.setLayout(new BorderLayout());
        setContentPane(JP);
        ShadePanel SP = new ShadePanel();
        JP.add(SP, BorderLayout.CENTER);
        SP.setLayout(null);

        JTextArea JT = new JTextArea("\nAuthor: Jocoboy\nEmail: Jocoboy@outlook.com");
        JT.setFocusable(false);
        JT.setEditable(false);
        JT.setOpaque(false);
        JT.setLineWrap(true);
        JT.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JT.setBounds(10, 10, 480, 160);
        SP.add(JT);

        JLabel Github_Title = new JLabel("Github repository:");
        Github_Title.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        JLabel Github_Link = new JLabel("https://github.com/Jocoboy/Matrix-Calculator");
        Github_Link.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        Github_Link.setBackground(Color.white);
        Github_Link.addMouseListener(new InternetMonitor());

        SP.add(Github_Title);
        Github_Title.setBounds(10, 10, 480, 320);
        SP.add(Github_Link);
        Github_Link.setBounds(10, 50, 480, 320);

        setVisible(true);
    }

    public static void main(String[] args) {
        new About();
    }
}