package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Tauler extends JPanel {

    public static final int DIMENSIO = 20;
    private static final int MAXIM = 500;
    private static final int COSTAT = MAXIM / DIMENSIO;
    private Casella[][] t;

    public Tauler() {
        t = new Casella[DIMENSIO][DIMENSIO];
        int y = 0;

        for (int i = 0; i < DIMENSIO; i++) {
            int x = 0;
            for (int j = 0; j < DIMENSIO; j++) {
                Rectangle2D.Float r = new Rectangle2D.Float(x, y, COSTAT, COSTAT);
                t[i][j] = new Casella(r, false); // Initialize as empty tiles
                x += COSTAT;
            }
            y += COSTAT;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / COSTAT;
                int col = e.getX() / COSTAT;
                if (row >= 0 && row < DIMENSIO && col >= 0 && col < DIMENSIO) {
                    // Toggle the occupation status of the cell
                    t[row][col].setOcupada(!t[row][col].isOcupada());
                    // Change the texture of the cell
                    t[row][col].setTexture("CHOCOLATE.jpg");
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < DIMENSIO; i++) {
            for (int j = 0; j < DIMENSIO; j++) {
                t[i][j].paintComponent(g2d);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAXIM, MAXIM);
    }
}

