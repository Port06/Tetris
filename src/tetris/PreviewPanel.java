package tetris;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {

    private TetrisPiece previewPiece;

    public PreviewPanel() {
        setPreferredSize(new Dimension(120, 120)); // Set the preferred size
    }

    public void setPreviewPiece(TetrisPiece piece) {
        this.previewPiece = piece;
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
}