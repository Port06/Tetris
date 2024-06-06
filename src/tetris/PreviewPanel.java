package tetris;

import javax.swing.*;
import java.awt.*;


//Clase que define el panel de la siguinete pieza en rotación
public class PreviewPanel extends JPanel {

    private TetrisPiece previewPiece;
    private TetrisGame tetrisGame;
    private Casella casella;

    public PreviewPanel(TetrisGame tetrisGame, Casella casella) {
        this.tetrisGame = tetrisGame;
        this.casella = casella;
        setPreferredSize(new Dimension(120, 120)); // Set the preferred size
        this.previewPiece = tetrisGame.getCurrentPiece();
    }
    
    //Getter y Setter necesarios
    public void setPreviewPiece(TetrisPiece previewPiece) {
        this.previewPiece = previewPiece;
        repaint();
    }
    
    public TetrisPiece getPreviewPiece() {
        return previewPiece;
    }

    //Metodos adicionales de la clase
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (previewPiece != null) {
            boolean[][] shape = previewPiece.getShape();
            int pieceWidth = previewPiece.getWidth();
            int pieceHeight = previewPiece.getHeight();

            int startX = (getWidth() - pieceWidth * 20) / 2; //Centrar horizontalmente
            int startY = (getHeight() - pieceHeight * 20) / 2; //Centrar verticalmente

            for (int i = 0; i < pieceHeight; i++) {
                for (int j = 0; j < pieceWidth; j++) {
                    if (shape[i][j]) {
                        ImageIcon texture = new ImageIcon(getClass().getResource(casella.getOcuppiedCellTexture())); //Carga de la textura
                        Image textureImage = texture.getImage();
                        g2d.drawImage(textureImage, startX + j * 20, startY + i * 20, 20, 20, null); //Dibujo de la textura
                    }
                }
            }
        }
    }
    
    
    //Metodos para rotar la pieza del panel
    public void rotateClockwise() {
        if (previewPiece != null) {
            previewPiece.rotateClockwise();
            repaint();
        }
    }

    public void rotateCounterClockwise() {
        if (previewPiece != null) {
            previewPiece.rotateCounterClockwise();
            repaint();
        }
    }
}