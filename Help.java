import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class Help extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel JP;
    private JScrollPane JS;
    
    public Help() {
        setTitle("Help");
        setSize(500,400);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JP = new JPanel();
        JP.setLayout(new BorderLayout());
        setContentPane(JP);
        ShadePanel SP = new ShadePanel();
        JP.add(SP,BorderLayout.CENTER);
        SP.setLayout(null);
        
        JTextArea JT = new JTextArea(
        "To defined a matrix, the following formats are supported:\n"+
        ">>a=[1 2 ; 3 4]\n"+
        ">>c = [ 9 10 11 12 ; 13 14 15          16 ]\n"+
        ">>d =[1 2 3,4 ;5   6 7 8; 9,10,11 12 ]\n\n"+
        "For simple arithmetic:\n"+
        ">>A+B (refer to Matrix Addition)\n"+
        ">>A-B (refer to Matrix Subtraction)\n"+
        ">>A*B (refer to Matrix Multiplication)\n"+
        ">>inv(A) (refer to Matrix Inverse)\n"+
        ">>A/B (refer to A*inv(B))\n\n"+
        "For other commands:\n"+
        ">>cls (to clear Cmd Text Area)\n"+
        ">>del all (to clear both Cmd Text Area and Workspace)\n"+
        ">>help (to see Help Window)\n"+
        ">>about (to see About Window)\n"+
        ">>exit (to close Application)\n\n"+
        "For more usage, see also in README."
        );

        JT.setFocusable(false);
        JT.setEditable(false);
        JT.setOpaque(false);
        JT.setLineWrap(true);
        JT.setFont(new Font("微软雅黑",Font.PLAIN,15));

        JS = new JScrollPane(JT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Border B = BorderFactory.createLineBorder(Color.gray, 2);
        TitledBorder TB = BorderFactory.createTitledBorder(B, "Command Usage", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("微软雅黑", Font.BOLD, 12));
        JS.setBorder(TB);
        JS.setOpaque(false);
        JS.getViewport().setOpaque(false);
        JS.setBounds(10,10,480,320);
        SP.add(JS);

        setVisible(true);
    }

    public static void main(String[] args){
        new Help();
    } 
}