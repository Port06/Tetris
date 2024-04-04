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

        // Add the preview panel to the frame
        frame.add(previewPanel, BorderLayout.EAST);

        // Create a container panel to center the Tetris panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create an empty panel to occupy vertical space above the Tetris board
        JPanel topSpacePanel = new JPanel();
        topSpacePanel.setPreferredSize(new Dimension(1200, 210)); // Adjust the height as needed
        containerPanel.add(topSpacePanel);

        // Add the Tetris board to the container panel
        containerPanel.add(tauler, BorderLayout.CENTER);

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