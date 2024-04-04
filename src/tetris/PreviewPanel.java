package tetris;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {
    private TetrisPiece previewPiece;

    public PreviewPanel() {
        setPreferredSize(new Dimension(100, 100)); // Adjust size as needed
    }

    public void setPreviewPiece(TetrisPiece piece) {
        this.previewPiece = piece;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the preview piece on the preview panel
        if (previewPiece != null) {
            boolean[][] shape = previewPiece.getShape();
            int tileSize = Math.min(getWidth() / 3, getHeight() / 3); // Calculate tile size to fit 3x3 grid
            int startX = (getWidth() - tileSize * 3) / 2;
            int startY = (getHeight() - tileSize * 3) / 2;
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j]) {
                        g2d.setColor(Color.BLACK); // Set color for filled tiles
                        g2d.fillRect(startX + j * tileSize, startY + i * tileSize, tileSize, tileSize);
                    }
                }
            }
        }
    }
}
