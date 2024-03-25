package tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris {

    public static void main(String[] args) {
        // Create a frame to hold the Tetris panel
        JFrame frame = new JFrame("Tetris");

        // Create an instance of the Tauler class
        Tauler tauler = new Tauler();

        // Create a container panel to center the Tetris panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        containerPanel.add(tauler);

        // Add the container panel to the frame
        frame.add(containerPanel);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // Set the initial frame size
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}