
package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameMenu {
    
    private static Tetris tetris;
    private static TetrisGame tetrisGame;
    private static Casella casella;
    private static Tauler tauler;
    private static PreviewPanel previewPanel;
    private static GameMenu gameMenu;
    private static GameIO gameIO;

    private static JFrame frame;
    private static JPanel gamePanel;
    private static JTextField scoreField;
    private static JProgressBar timeBar;
    private static Timer gameTimer;
    private static JPanel sidePanel;
    private static JPanel topPanel;

    // Method to initialize the Tetris instance
    public GameMenu(GameIO gameIO) {
        this.gameIO = gameIO;
    }
    
    public void setTetris(Tetris tetris) {
        this.tetris = tetris;
    }
    
    public static void startGame() {
        // Ensure all necessary variables are properly initialized
        tetrisGame = tetris.getTetrisGame();
        frame = Tetris.frame;
        sidePanel = Tetris.getSidePanel();
        topPanel = Tetris.getTopPanel();

        // Disable buttons except the exit button
        SwingUtilities.invokeLater(() -> tetris.setButtonsAndIconsEnabled(false));

        // Define the game as active
        tetris.setIsGameActive(true);

        // Initialize game components
        previewPanel = new PreviewPanel(tetrisGame, casella);
        tauler = new Tauler(previewPanel, tetrisGame, tetris, gameMenu);

        tetrisGame.setPreviewPanel(previewPanel);
        tetrisGame.setTauler(tauler);

        // Initialize the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 100));

        JTextField playerNameField = new JTextField("Name: " + tetrisGame.getPlayerName());
        playerNameField.setEditable(false);
        playerNameField.setBackground(Color.BLACK);
        playerNameField.setForeground(Color.WHITE);
        playerNameField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Increase font size

        scoreField = new JTextField("Score: " + tetrisGame.getPlayerScore());
        scoreField.setEditable(false);
        scoreField.setBackground(Color.BLACK);
        scoreField.setForeground(Color.WHITE);
        scoreField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        scoreField.setFont(new Font("Arial", Font.PLAIN, 18)); // Change font size

        // Define the dimensions of the containers
        Dimension fieldSize = new Dimension(200, 40); // Adjust height
        playerNameField.setPreferredSize(fieldSize);
        scoreField.setPreferredSize(fieldSize);

        // Initialize the time bar to the value specified in the configuration
        timeBar = new JProgressBar(0, tetrisGame.getTotalGameTime());
        timeBar.setValue(tetrisGame.getTotalGameTime());
        timeBar.setForeground(Color.GREEN); // Change progress bar color here

        // Define the size of the time bar
        Dimension progressBarSize = new Dimension(frame.getWidth(), 20); // Adjust height here
        timeBar.setPreferredSize(progressBarSize);

        bottomPanel.add(playerNameField);
        bottomPanel.add(scoreField);
        bottomPanel.add(timeBar);

        // Set up the main game panel
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(tauler);

        // Load the right panel image
        Image fondoImage = new ImageIcon(Tetris.class.getResource("/fondo.jpg")).getImage();
        if (fondoImage == null) {
            System.err.println("Fondo image not found.");
        }

        JPanel previewMainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondoImage != null) {
                    g.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        JButton rotarFichaButton = new JButton(new ImageIcon("assets/iconoBotonRotar.jpg"));
        JButton cambiarFichaButton = new JButton(new ImageIcon("assets/iconoBotonNuevaForma.jpg"));

        rotarFichaButton.setBackground(Color.BLACK);
        rotarFichaButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        cambiarFichaButton.setBackground(Color.BLACK);
        cambiarFichaButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        rotarFichaButton.addActionListener(e -> {
            if (tetrisGame.getCurrentPiece() != null) {
                tetrisGame.getCurrentPiece().rotateClockwise();
                previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getRotateFormScoreCost()); // Penalize player
                scoreField.setText("Score: " + tetrisGame.getPlayerScore());
                previewPanel.repaint();
            }
        });

        cambiarFichaButton.addActionListener(e -> {
            tetrisGame.updatePiece();
            tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getChangeFormCost()); // Penalize player
            scoreField.setText("Score: " + tetrisGame.getPlayerScore());
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        previewMainPanel.add(rotarFichaButton, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        previewMainPanel.add(previewPanel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        previewMainPanel.add(cambiarFichaButton, gbc);

        // Set up the game panel layout
        gamePanel.add(mainPanel, BorderLayout.CENTER);
        gamePanel.add(previewMainPanel, BorderLayout.EAST);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
        gamePanel.add(sidePanel, BorderLayout.WEST); // Re-add the side panel
        gamePanel.add(topPanel, BorderLayout.NORTH); // Re-add the top panel

        frame.getContentPane().removeAll();
        frame.getContentPane().add(gamePanel);
        frame.revalidate();
        frame.repaint();

        // Start the game timer
        tetrisGame.setGameTime(tetrisGame.getTotalGameTime());
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisGame.setGameTime(tetrisGame.getGameTime() - 1);
                timeBar.setValue(tetrisGame.getGameTime());
                if (tetrisGame.getGameTime() <= 0) {
                    gameTimer.stop();
                    endGame();
                }
            }
        });

        SwingUtilities.invokeLater(() -> tauler.requestFocusInWindow());

        // Start the game timer
        gameTimer.start();
    }
    
    

    // Method to increase the score
    public static void increaseScore() {
        tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getRemoveCellCost());
        scoreField.setText("Score: " + tetrisGame.getPlayerScore());
    }
    
    

    // Method to handle the end of the game
    private static void endGame() {
        // Define the game as finished
        tetris.setIsGameActive(false);

        // Create an instance of a completed game for the record
        Game completedGame = new Game(
            tetrisGame.getPlayerName(),
            LocalDateTime.now(),
            tetrisGame.getTotalGameTime() - tetrisGame.getGameTime(), // Total time played
            tetrisGame.getPlayerScore()
        );

        // Add the completed game to the record of played games
        gameIO.addCompletedGame(completedGame);

        // Show a dialog with the player's score and total time
        JOptionPane.showMessageDialog(
            frame,
            "Game over! " + tetrisGame.getPlayerName() + " Score: " + tetrisGame.getPlayerScore() + " Time: " + (tetrisGame.getTotalGameTime() - tetrisGame.getGameTime()) + " seconds",
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Clear the game panel and return to the main menu
        gamePanel.removeAll();
        frame.getContentPane().removeAll();
        tetris.showMainMenu();
    }
}
