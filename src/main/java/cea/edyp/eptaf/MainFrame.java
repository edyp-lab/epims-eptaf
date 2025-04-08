package cea.edyp.eptaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private static final int MAX_SIZE = 40000;

    private static JTextArea m_textArea;

    private static StringBuilder m_sb = new StringBuilder(MAX_SIZE);

    public MainFrame() {
        super("eP-TAF");

        ArrayList<Image> icons = new ArrayList();
        Image icon = loadImage("eptaf-64.png");
        icons.add(icon);
        setIconImages(icons);

        setResizable(true);
        setSize(new Dimension(500,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(constructPane());
    }

    public static synchronized void append(String log) {
        synchronized(m_sb) {
            if (m_sb.length() > MAX_SIZE-MAX_SIZE/10) {
                String s = m_sb.substring(MAX_SIZE/10);
                m_sb.setLength(0);
                m_sb.append(s);
            }
            m_sb.append(log);

            setText(m_sb.toString());
        }
    }

    private static synchronized void setText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                synchronized(m_sb) {
                    if (m_textArea != null) {
                        m_textArea.setText(text);
                    }
                }
            }
        });
    }

    private JScrollPane constructPane() {
        m_textArea = new JTextArea();
        synchronized(m_sb) {
            m_textArea.setText(m_sb.toString());
        }
        m_textArea.setEditable(false);
        m_textArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(m_textArea);
        return scrollPane;
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(path));
        } catch (IOException e) {

        }
        return null;
    }
}
