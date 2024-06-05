package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class Casella {
    private Rectangle2D.Float rec;
    private ImageIcon texture;
    private ImageIcon textureOcu;
    private boolean ocupada;
    
    //Textura por defecto de una casilla rellena
    private static String ocuppiedCellTexture = "/CHOCOLATE.jpg";
    //Textura por defecto de una casilla
    private String emptyCellTexture = "/LIBRE.jpg";

    public Casella(Rectangle2D.Float r, boolean ocu) {
        
        this.rec = r;
        this.ocupada = ocu;
        
        this.texture = new ImageIcon(getClass().getResource(emptyCellTexture));
        this.textureOcu = new ImageIcon(getClass().getResource(ocuppiedCellTexture));
    }
    
    //Uso de la clase Grafics2D para el dibiju de los cuadrados (casillas)
    public void paintComponent(Graphics2D g2d) {
        if (ocupada && texture != null) {
            g2d.drawImage(textureOcu.getImage(), (int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight(), null);
        } else {
            g2d.drawImage(texture.getImage(), (int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight(), null);
        }
    }
    
    //Getters y setters
    public Rectangle2D.Float getRec() {
        return rec;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
    
    public void setEmpty(boolean ocupada) {
        this.ocupada = !ocupada;
    }

    public ImageIcon getTexture() {
        return texture;
    }
    
    public static void setOcuppiedCellTexture(String ocuppiedCellTexture) {
        Casella.ocuppiedCellTexture = ocuppiedCellTexture;
    }

    public static String getOcuppiedCellTexture() {
        return ocuppiedCellTexture;
    }
    
    //Metodo que cambia la textura de la casilla al rellenarla
    public void setTexture() {
        this.texture = new ImageIcon(getClass().getResource(ocuppiedCellTexture));
    }
}

