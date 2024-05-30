package tetris;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {

    private TetrisPiece previewPiece;
    private TetrisGame tetrisGame;

    public PreviewPanel(TetrisGame tetrisGame) {
        this.tetrisGame = tetrisGame;
        setPreferredSize(new Dimension(120, 120)); // Set the preferred size
        this.previewPiece = tetrisGame.getCurrentPiece();
    }

    public void setPreviewPiece(TetrisPiece previewPiece) {
        this.previewPiece = previewPiece;
        repaint();
    }
    
    public TetrisPiece getPreviewPiece() {
        return previewPiece;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (previewPiece != null) {
            boolean[][] shape = previewPiece.getShape();
            int pieceWidth = previewPiece.getWidth();
            int pieceHeight = previewPiece.getHeight();

            int startX = (getWidth() - pieceWidth * 20) / 2; // Center horizontally
            int startY = (getHeight() - pieceHeight * 20) / 2; // Center vertically

            for (int i = 0; i < pieceHeight; i++) {
                for (int j = 0; j < pieceWidth; j++) {
                    if (shape[i][j]) {
                        ImageIcon texture = new ImageIcon(getClass().getResource("/CHOCOLATE.jpg")); // Load texture from resource
                        Image textureImage = texture.getImage();
                        g2d.drawImage(textureImage, startX + j * 20, startY + i * 20, 20, 20, null); // Draw the texture
                    }
                }
            }
        }
    }
    
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