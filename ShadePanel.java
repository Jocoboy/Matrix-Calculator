import javax.swing.*;

import java.awt.*;
import java.awt.Graphics2D;

public class ShadePanel extends JPanel {

    private static final long serialVersionUID = -3764466251368925570L;

    public ShadePanel() {
        super();
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, height, Color.GRAY);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}