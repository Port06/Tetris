package tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris {

    public static void main(String[] args) {
        // Create a frame to hold the Tetris panel
        JFrame frame = new JFrame("Tetris");

        // Create an instance of the Tetris board
        PreviewPanel previewPanel = new PreviewPanel(); // Create previewPanel first
        Tauler tauler = new Tauler(previewPanel); // Pass the previewPanel instance

        // Load the texture image for the preview panel
        ImageIcon fondoTexture = new ImageIcon(Tetris.class.getResource("/FONDO.jpg"));
        final Image fondoImage = fondoTexture.getImage(); // Make fondoImage final

        // Create a container panel with BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());

        // Create a panel to hold the Tetris board and center it horizontally
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(tauler);

        // Create a panel to hold the preview panel and center it horizontally
        JPanel previewMainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int imageX = (getWidth() - fondoImage.getWidth(null)) / 2;
                int imageY = (getHeight() - fondoImage.getHeight(null)) / 2;
                g.drawImage(fondoImage, imageX, imageY, this);
            }
        };
        
        // Add previewPanel to the previewMainPanel, centering it vertically
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight= GridBagConstraints.REMAINDER; // Make it span multiple rows
        previewMainPanel.add(previewPanel, gbc);

        // Add the main panel to the container panel on the left
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // Set the background color of the main panel to black
        mainPanel.setBackground(Color.BLACK);

        // Add the preview main panel to the container panel on the right
        containerPanel.add(previewMainPanel, BorderLayout.EAST);

        // Add the container panel to the frame
        frame.add(containerPanel);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800); // Set the initial frame size
        frame.setMinimumSize(new Dimension(700, 700)); // Set minimum size
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

        // Update the preview panel with the next piece
        TetrisPiece nextPiece = tauler.selectRandomPiece();
        previewPanel.setPreviewPiece(nextPiece);
    }
}