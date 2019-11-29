import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.regex.*;
import java.text.NumberFormat;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private static JTextArea J211;
    private static JTextArea J221;
    private static CmdTextArea workspace;
    private static int ptr_workspace;

    private static int currentDot;
    private static int currentKeyCode;
    private static int currentPos;
    private static boolean isAllowedInputArea;
    private static boolean isConsume;
    private static StringBuffer textBuffer;

    /********************** const variable ****************************/
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;
    private static final int SCROLL_WIDTH = 500;
    private static final int SCROLL_HEIGHT = 600;
    private static final int VIEWPORT_WIDTH = 393;
    private static final int VIEWPORT_HEIGHT = 600;
    private static final int WORKSPACE_FONT_SIZE = 15;
    private static final int BUTTON_FONT_SIZE = 20;

    private static final int DEFAULT_ARRAY_SIZE = 100;
    private static final int DEFAULT_MatrixARRAY_SIZE = 10;

    /********************** const variable ****************************/

    public static class CmdTextArea extends JTextArea implements KeyListener, CaretListener {

        private static final long serialVersionUID = 1L;

        private static Matrix[] matrixBuffer = new Matrix[DEFAULT_MatrixARRAY_SIZE];

        /****************************** RegExp ***************************************/

        /**
         * Regular expression for standard matrix input,modelled from Matlab.
         */
        String pattern = "^\\s*[A-Za-z][A-Za-z\\d]*\\s*=\\s*\\["
                + "(((\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*,)|(\\s*(\\+|-)?\\d+(\\.\\d+)?\\s+(,)?))*\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*;)*"
                + "((\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*,)|(\\s*(\\+|-)?\\d+(\\.\\d+)?\\s+(,)?))*\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*"
                + "\\]\\s*$";
        /**
         * Regular expression for command 'inv(A)' or 'INV(A)' or 'inverse(A)' or
         * "INVERSE(A)". A refers to the defined matrix.
         */
        String pattern_inv = "^\\s*(inv|INV|inverse|INVERSE)\\s*\\(\\s*[A-Za-z][A-Za-z\\d]*\\s*\\)\\s*$";

        /****************************** RegExp ***************************************/
        public CmdTextArea() {
            super();
        }

        private boolean checkConsume(KeyEvent e) {
            if (!isAllowedInputArea) {
                e.consume();
                return true;
            }
            if ((currentKeyCode == KeyEvent.VK_BACK_SPACE || currentKeyCode == KeyEvent.VK_ENTER
                    || currentKeyCode == KeyEvent.VK_UP || currentKeyCode == KeyEvent.VK_LEFT)
                    && currentDot == textBuffer.length()) {
                e.consume();
                return true;
            }
            return false;
        }

        public Matrix isStandardMatrix(String input) {
            int row = 0;
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == ';') {
                    row++;
                }
            }
            int[] columnsForEach = new int[++row];
            double[] outputArray_temp = new double[DEFAULT_ARRAY_SIZE];

            int splitPos = input.indexOf("[");
            String p1 = input.substring(0, splitPos).replaceAll(" ", "");
            String name = p1.substring(0, p1.length() - 1);
            String p2 = input.substring(splitPos, input.length());

            int ptr = 0, ptr_temp = 0, pos_temp = 0, columns = 0;
            String numberBuffer = "";
            char ch = p2.charAt(ptr++);
            for (int i = 0; i < row; i++) {
                while (ch != ';' && ch != ']') {
                    if (ch >= '0' && ch <= '9' || ch == '+' || ch == '-') {
                        ptr_temp = ptr;
                        numberBuffer += ch;
                        while (p2.charAt(ptr_temp) >= '0' && p2.charAt(ptr_temp) <= '9' || p2.charAt(ptr_temp) == '.') {
                            numberBuffer += p2.charAt(ptr_temp);
                            ptr_temp++;
                        }
                        outputArray_temp[pos_temp++] = Double.valueOf(numberBuffer).doubleValue();
                        numberBuffer = "";// Clear numberBuffer.
                        ptr = ptr_temp;// Move to next positon.
                        columns++;
                    }
                    ch = p2.charAt(ptr++);
                }
                columnsForEach[i] = columns;
                columns = 0;
                if (i < row - 1) {
                    ch = p2.charAt(ptr++);
                }
            }

            int column = 0;
            for (int i = 1; i < row; i++) {
                // The number of row is not equal to column.
                if (columnsForEach[i] != column) {
                    System.out.println("Error!");
                    return null;
                }
            }

            double[][] outputArray = new double[row][column];
            pos_temp = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    outputArray[i][j] = outputArray_temp[pos_temp++];
                }
            }
            return new Matrix(name, outputArray);
        }

        public boolean isStandardExpression(String expression) {
            // TO DO
            return false;
        }

        public Matrix evaluateExpression(String expression) throws MatrixArithException {
            Matrix outputMatrix = null;
            // TO DO

            return outputMatrix;
        }

        @Override
        public void append(String msg) {
            super.append(msg);
            textBuffer.append(msg);
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            currentDot = e.getDot();
            System.out.println("currentDot : " + currentDot);
            isAllowedInputArea = currentDot < textBuffer.length() ? false : true;
            System.out.println("isAllowedInput : " + isAllowedInputArea);
            System.out.println("isConsume : " + isConsume);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (isConsume) {
                e.consume();
                currentPos = this.getText().length();
                this.setCaretPosition(currentPos);
                System.out.println("Consumed!");
                return;
            }

            if (currentKeyCode == KeyEvent.VK_ENTER) {
                String input = this.getText().substring(textBuffer.length(), this.getText().length() - 1);
                System.out.println("Input command : " + input);
                textBuffer.append(input);
                textBuffer.append("\n");
                /*********************** Command***************************** */
                if (input.equals("exit")) {
                    this.append("Bye!");
                    System.exit(0);
                }
                else if(input.equals("cls")||input.equals("clear")||input.equals("CLS")||input.equals("CLEAR")){
                    this.setText("");
                    textBuffer.delete(0, textBuffer.length());
                    this.append(">>");
                }
                 else if (input.equals("del all") || input.equals("delete all") || input.equals("DEL ALL")
                        || input.equals("DELETE ALL")) {
                    J211.setText("");
                    ptr_workspace = 0;
                } else if (input.equals("about") || input.equals("ABOUT")) {
                    this.append(input);
                    this.append("\n\n");
                    this.append(">>");
                    new About();
                } else if (input.equals("help") || input.equals("HELP")) {
                    this.append(input);
                    this.append("\n\n");
                    this.append(">>");
                    new Help();
                } else if (Pattern.matches(pattern, input)) {
                    System.out.println("Pattern matched!");

                    if (isStandardMatrix(input) == null) {
                        System.out.println("Invalid input.");
                        this.append("\n\n");
                        this.append(">>");
                    } else {
                        System.out.println("Valid input.");
                        Matrix matrix = isStandardMatrix(input);
                        int row = matrix.array.length;
                        int column = matrix.array[0].length;
                        System.out.println(matrix.name + ":");
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < column; j++) {
                                System.out.println(matrix.array[i][j] + " ");
                            }
                            System.out.println();
                        }

                        boolean flag = false;
                        NumberFormat nFormat = NumberFormat.getNumberInstance();
                        nFormat.setMaximumFractionDigits(2);
                        nFormat.setMinimumFractionDigits(2);

                        for (int i = 0; i < ptr_workspace; i++) {
                            if (matrixBuffer[i].name.equals(matrix.name)) {
                                matrixBuffer[i] = matrix;
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            matrixBuffer[ptr_workspace++] = matrix;
                            System.out.println(ptr_workspace);
                        }
                        if (ptr_workspace == 0) {
                            matrixBuffer[ptr_workspace++] = matrix;
                        }
                        String J211Text = "";
                        J211.setText("");
                        for (int i = 0; i < ptr_workspace; i++) {
                            J211Text += matrixBuffer[i].name + " = ";
                            for (int j = 0; j < matrixBuffer[i].array.length; j++) {
                                for (int k = 0; k < matrixBuffer[i].array[0].length; k++) {
                                    J211Text += nFormat.format(matrixBuffer[i].array[j][k]) + " ";
                                }
                                J211Text += "\n";
                                // Append blank areas to n-1 rows.
                                if (j != matrixBuffer[i].array.length - 1) {
                                    for (int len = 0; len < matrixBuffer[i].name.length() + 4; len++) {
                                        J211Text += " ";
                                    }
                                }
                            }
                            J211Text += "\n";
                        }
                        J211.setText(J211Text);

                        String commandText = "";
                        commandText += matrix.name + " = ";
                        for (int j = 0; j < matrix.array.length; j++) {
                            for (int k = 0; k < matrix.array[0].length; k++) {
                                commandText += nFormat.format(matrix.array[j][k]) + " ";
                            }
                            commandText += "\n";
                            // Append blank areas to n-1 rows.
                            if (j != matrix.array.length - 1) {
                                for (int len = 0; len < matrix.name.length() + 4; len++) {
                                    commandText += " ";
                                }
                            }
                        }
                        this.append(commandText);
                        this.append("\n\n");
                        this.append(">>");
                    }
                } else if (Pattern.matches(pattern_inv, input)) {
                    String name="";
                    Matrix ouputMatrix= null;
                    try{
                        int l_pos = input.indexOf("(");
                        int r_pos = input.indexOf(")");
                        name = input.substring(l_pos+1,r_pos).replace(" ", "");

                        for(int i = 0 ; i <ptr_workspace;i++){
                            if(matrixBuffer[i].name.equals(name)){
                                ouputMatrix = MatrixArith.inv(matrixBuffer[i]);
                                break;
                            }
                            else{
                                if(i == ptr_workspace -1){
                                    ouputMatrix = new Matrix("null", null);
                                    throw new MatrixArithException("Matrix \""+name+"\" is undefined!");
                                }
                            }
                        }

                        String commandText = "";
                        NumberFormat nFormat=NumberFormat.getNumberInstance(); 
					    nFormat.setMaximumFractionDigits(2);
					    nFormat.setMinimumFractionDigits(2);
                        for(int i = 0 ; i < ouputMatrix.array.length;i++){
                            for(int j = 0 ; j < ouputMatrix.array[0].length;j++){
                                commandText += nFormat.format(ouputMatrix.array[i][j] +" ");
                            }
                            commandText += "\n";
                        }
                        this.append(commandText);
                        this.append("\n\n");
                        this.append(">>");
                        String commandText_last = J221.getText();
                        commandText_last += input.replaceAll(" ", "") +  "= \n" + commandText+ "\n";
                        J221.setText(commandText_last);
                    }catch(MatrixArithException err){
                        System.out.println(err.toString());
                        this.append("Matrix \""+name+"\" cannot be inversed.");
                        this.append(">>");
                        String commandText_last = J221.getText();
                        commandText_last += input +  " = \n" + "Error!" + "\n\n";
                        J221.setText(commandText_last);
                    }
                } else if (isStandardExpression(input)) {
                    Matrix ouputMatrix= null;
                   
                    try{
                        ouputMatrix = evaluateExpression(input + "=");

                        String commandText = "";
                        NumberFormat nFormat=NumberFormat.getNumberInstance(); 
					    nFormat.setMaximumFractionDigits(2);
                        nFormat.setMinimumFractionDigits(2);
                        for(int i = 0 ; i < ouputMatrix.array.length;i++){
                            for(int j = 0 ; j < ouputMatrix.array[0].length;j++){
                                commandText += nFormat.format(ouputMatrix.array[i][j] +" ");
                            }
                            commandText += "\n";
                        }
                        this.append(commandText);
                        this.append("\n\n");
                        this.append(">>");
                        String commandText_last = J221.getText();
                        commandText_last += input.replaceAll(" ", "") +  "= \n" + commandText+ "\n";
                        J221.setText(commandText_last);
                    }catch(MatrixArithException err){
                        System.out.println(err.toString());
                        String err_msg = err.toString();
                        int l_pos = err_msg.lastIndexOf(":")+2;
                        int r_pos = err_msg.length();
                        err_msg = err_msg.substring(l_pos, r_pos);

                        this.append(err_msg);
                        this.append("\n\n");
                        this.append(">>");
                        String commandText_last = J221.getText();
                        commandText_last += input +  " = \n" + "Error!" + "\n\n";
                        J221.setText(commandText_last);
                    }
                } else {
                    this.append("Command \"" + input + "\" is not found!");
                    this.append("\n\n");
                    this.append(">>");
                }
                /*********************** Command***************************** */
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            currentKeyCode = e.getKeyCode();
            isConsume = checkConsume(e) ? true : false;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (isConsume) {
                e.consume();
            }
        }

    }

    public MainWindow() {

        // Set some basic configurations.
        super("Matrix Calculator");
        Image img = Toolkit.getDefaultToolkit().getImage("favicon.jpg");
        setIconImage(img);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);// Place the main window in the middle.
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
        linewrapItem.setSelected(false);
        setMenu.add(linewrapItem);
        setMenu.add(resetItem);
        getMenu.add(helpItem);
        getMenu.add(aboutItem);
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
                currentDot = -1;
                isAllowedInputArea = false;
                isConsume = false;
                textBuffer = new StringBuffer();
                workspace.setText("");
                workspace.append(">>");
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

        textBuffer = new StringBuffer();
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
        TitledBorder tBorder22 = BorderFactory.createTitledBorder(border22, "Command History", TitledBorder.LEFT,
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

        J21.add(JS211, BorderLayout.CENTER);
        J21.add(J212, BorderLayout.SOUTH);

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

        J22.add(JS221, BorderLayout.CENTER);
        J22.add(J222, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }

}