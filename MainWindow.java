import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;
    private static final int SCROLL_WIDTH = 500;
    private static final int SCROLL_HEIGHT = 600;
    private static final int VIEWPORT_WIDTH = 393;
    private static final int VIEWPORT_HEIGHT = 600;
    private static final int WORKSPACE_FONT_SIZE = 15;
    private static final int BUTTON_FONT_SIZE = 20;

    public static class CmdTextArea extends JTextArea implements KeyListener, CaretListener {

        private static final long serialVersionUID = 1L;

        public CmdTextArea() {
            super();
        }
    }

    public MainWindow() {

        // Set some basic configurations.
        super("Matrix Calculator");
        Image img = Toolkit.getDefaultToolkit().getImage("favicon.jpg");
        setIconImage(img);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocation(null);// Place the main window in the middle.
        setLayout(null);// Clear the default layout.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        /**
         * Set the menu bar. 
                     ┌── linewrapItem
                ┌── setMenu 
                |       └── resetItem 
            menuBar 
                |       ┌── helpItem
                └── getMenu 
                        └── abouItem
         */
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);
        setJMenuBar(menuBar);
        JMenu setMenu = new JMenu("Settings");
        JMenu getMenu = new JMenu("Help and About");
        JCheckBoxMenuItem linewrapItem = new JCheckBoxMenuItem("Linewrap");
        JMenuItem resetItem = new JMenuItem("Reset");
        JMenuItem helpItem = new JMenuItem("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        setMenu.add(linewrapItem);
        setMenu.add(resetItem);
        getMenu.add(helpItem);
        getMenu.add(aboutItem);
        linewrapItem.setSelected(false);
        menuBar.add(setMenu);
        menuBar.add(getMenu);

        linewrapItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (linewrapItem.getState()) {
                    workspace.setLineWrap(true);// for line
                    workspace.setWrapStyleWord(true);// for word
                } else {
                    workspace.setLineWrap(false);// for line
                    workspace.setWrapStyleWord(false);// for word
                }
            }
        });

        resetItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TO DO
                workspace.setText("");
                workspace.appen(">>");
                workspace.requestFocus();// Get focus.
                workspace.setCaretPosition(workspace.getText().length()); // Set focus at the end of text.
            }
        });

        helpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Help();
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new About();
            }
        });

        workspace = new CmdTextArea();
        workspace.addKeyListener(workspace);
        workspace.addCaretListener(workspace);
        workspace.setFont(new Font("宋体", Font.BOLD, WORKSPACE_FONT_SIZE));

        workspace.append(">>");
        workspace.requestFocus();
        workspace.setCaretPosition(workspace.getText().length());

        JScrollPane JS1 = new JScrollPane(workspace, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JS1.setBounds(0, 0, SCROLL_WIDTH, SCROLL_HEIGHT);
        add(JS1);

        JPanel J2 = new JPanel();
        J2.setLayout(new GridLayout(2, 1, 0, 2));
        J2.setBackground(Color.white);
        Border border2 = BorderFactory.createLineBorder(Color.black, 2);
        TitledBorder tBorder2 = BorderFactory.createTitledBorder(border2, "", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 0));// Remove title.
        J2.setBorder(tBorder2);
        J2.setBounds(SCROLL_WIDTH, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        add(J2);

        JPanel J21 = new JPanel();
        J21.setLayout(new BorderLayout());
        J21.setBackground(Color.white);
        Border border21 = BorderFactory.createLineBorder(Color.gray, 2);
        TitledBorder tBorder21 = BorderFactory.createTitledBorder(border21, "Workspace", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("微软雅黑", Font.BOLD, WORKSPACE_FONT_SIZE));
        J21.setBorder(tBorder21);

        JPanel J22 = new JPanel();
        J22.setLayout(new BorderLayout());
        J22.setBackground(Color.white);
        Border border22 = BorderFactory.createLineBorder(Color.gray, 2);
        TitledBorder tBorder22 = BorderFactory.createTitledBorder(border21, "Command History", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("微软雅黑", Font.BOLD, WORKSPACE_FONT_SIZE));
        J22.setBorder(tBorder22);

        J2.add(J21);
        J2.add(J22);

        J211 = new JTextArea();
        JScrollPane JS211 = new JScrollPane(J211, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        J211.setFont(new Font("宋体", Font.BOLD, WORKSPACE_FONT_SIZE));
        J211.setEditable(false);
        J211.setLineWrap(true);
        J211.setVisible(true);

        JButton J212 = new JButton("Clear Workspace");
        J212.setFont(new Font("微软雅黑", Font.BOLD, BUTTON_FONT_SIZE));
        J212.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                J211.setText("");
            }
        });

        J21.add(JS211,BorderLayout.CENTER);
        J21.add(J212,BorderLayout.SOUTH);

        J221 = new JTextArea();
        JScrollPane JS221 = new JScrollPane(J221, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        J221.setFont(new Font("宋体", Font.BOLD, WORKSPACE_FONT_SIZE));
        J221.setEditable(false);
        J221.setLineWrap(true);
        J221.setVisible(true);

        JButton J222 = new JButton("Clear Workspace");
        J222.setFont(new Font("微软雅黑", Font.BOLD, BUTTON_FONT_SIZE));
        J222.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                J221.setText("");
            }
        });

        J22.add(JS221,BorderLayout.CENTER);
        J22.add(J222,BorderLayout.SOUTH);

        setVisible(true);
    }
}