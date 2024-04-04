package tetris;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {
    private TetrisPiece previewPiece;
    public static final int DIMENSIO_PREVIEW = 3;
    private static final int MAXIM_PREVIEW = 150; // Maximum size of the preview panel
    private static final int SQUARE_SIZE_PREVIEW = MAXIM_PREVIEW / DIMENSIO_PREVIEW;

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

        // Draw the preview piece board
        for (int i = 0; i < DIMENSIO_PREVIEW; i++) {
            for (int j = 0; j < DIMENSIO_PREVIEW; j++) {
                if (previewPiece != null && previewPiece.getShape()[i][j]) {
                    // Draw the texture image at the appropriate location
                    Image textureImage = new ImageIcon(getClass().getResource("/CHOCOLATE.jpg")).getImage();
                    g2d.drawImage(textureImage, j * SQUARE_SIZE_PREVIEW, i * SQUARE_SIZE_PREVIEW, SQUARE_SIZE_PREVIEW, SQUARE_SIZE_PREVIEW, null);
                } else {
                    // Draw empty squares
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(j * SQUARE_SIZE_PREVIEW, i * SQUARE_SIZE_PREVIEW, SQUARE_SIZE_PREVIEW, SQUARE_SIZE_PREVIEW);
                }
            }
        }
    }
}