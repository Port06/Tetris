package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class Casella {
    private Rectangle2D.Float rec;
    private ImageIcon texture;
    private boolean ocupada;

    public Casella(Rectangle2D.Float r, boolean ocu) {
        this.rec = r;
        this.ocupada = ocu;
        this.texture = new ImageIcon(getClass().getResource("/LIBRE.jpg"));
    }

    public void paintComponent(Graphics2D g2d) {
        if (ocupada && texture != null) {
            g2d.drawImage(texture.getImage(), (int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight(), null);
        } else {
            g2d.setColor(Color.WHITE); // Definimos el color a blanco cuando la casilla esta vacia
            g2d.fill(rec);
        }
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

    public ImageIcon getTexture() {
        return texture;
    }
    
    //Metodo que cambia la textura de la casilla
    public void setTexture(String textureFileName) {
        this.texture = new ImageIcon(getClass().getResource("/CHOCOLATE.jpg"));
    }
}

