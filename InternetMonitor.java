import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

public class InternetMonitor extends MouseAdapter {

    public void mouseClicked(MouseEvent e) {
        JLabel JL = (JLabel) e.getSource();
        String text = JL.getText();
        System.out.println(text);
        URI uri;
        try {
            uri = new URI(text);
            Desktop desk = Desktop.getDesktop();
            if (Desktop.isDesktopSupported() && desk.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desk.browse(uri);
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        } catch (URISyntaxException err) {
            err.printStackTrace();
        }
    }

    public void mouseEntered(MouseEvent e) {
        JLabel JL = (JLabel) e.getSource();
        JL.setForeground(Color.RED);
    }

    public void mouseExited(MouseEvent e) {
        JLabel JL = (JLabel) e.getSource();
        JL.setForeground(Color.BLACK);
    }
}