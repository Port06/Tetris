package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Casella {
    
    private Rectangle2D.Float rec;
    private Color col;
    private boolean ocupada;

    public Casella(Rectangle2D.Float r, Color c, boolean ocu ) {
        this.rec = r;
        this.col = c;
        this.ocupada = ocu;
    }

    public void paintComponent(Graphics2D g2d) {
        g2d.setColor(this.col);
        g2d.fill(this.rec);
    }

    public Rectangle2D.Float getRec() {
        return rec;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public Color getColor() {
        return col;
    }

    public void setColor(Color col) {
        this.col = col;
    }
}

