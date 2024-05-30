package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tetris {

    public static void main(String[] args) {
        // Create a frame to hold the Tetris panel
        JFrame frame = new JFrame("Tetris");
        
        // Create TetrisGame instance
        TetrisGame tetrisGame = new TetrisGame();

        // Create PreviewPanel instance
        PreviewPanel previewPanel = new PreviewPanel(tetrisGame);

        // Pass the previewPanel and TetrisGame instances to Tauler
        Tauler tauler = new Tauler(previewPanel, tetrisGame);
        
        // Set the PreviewPanel instance in the TetrisGame
        tetrisGame.setPreviewPanel(previewPanel);
        tetrisGame.setTauler(tauler);
        
        

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
        
        // Create and configure the "Rotate Piece" and "Change Piece" buttons
        JButton rotarFichaButton = new JButton(new ImageIcon("assets/iconoBotonRotar.jpg"));
        JButton cambiarFichaButton = new JButton(new ImageIcon("assets/iconoBotonNuevaForma.jpg"));

        // Configure the style of the buttons to be consistent
        rotarFichaButton.setBackground(Color.BLACK);
        rotarFichaButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        cambiarFichaButton.setBackground(Color.BLACK);
        cambiarFichaButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Set placeholder textures for the buttons
        rotarFichaButton.setIcon(new ImageIcon("assets/iconoBotonRotar.jpg"));
        cambiarFichaButton.setIcon(new ImageIcon("assets/iconoBotonNuevaForma.jpg"));

        // Adjust button sizes to be consistent
        Dimension buttonSize = new Dimension(100, 100); // Example size for square buttons
        rotarFichaButton.setPreferredSize(buttonSize);
        cambiarFichaButton.setPreferredSize(buttonSize);

        // Add buttons and preview panel to previewMainPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0; // Position the button above the preview panel
        gbc.insets = new Insets(10, 0, 10, 0); // Add padding
        previewMainPanel.add(rotarFichaButton, gbc);

        gbc.gridy = 1; // Position the preview panel
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        previewMainPanel.add(previewPanel, gbc);

        gbc.gridy = 2; // Position the button below the preview panel
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        previewMainPanel.add(cambiarFichaButton, gbc);


        gbc.gridy = 2; // Position the button below the preview panel
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        previewMainPanel.add(cambiarFichaButton, gbc);
        
        // Add action listeners for the buttons
        rotarFichaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tetrisGame.getCurrentPiece() != null) {
                    tetrisGame.getCurrentPiece().rotateClockwise();
                    previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                    previewPanel.repaint();
                }
            }
        });

        cambiarFichaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tetrisGame.updatePiece();
            }
        });
        
        // Create side panel for the menu and buttons
        JPanel sidePanel = new JPanel(new GridLayout(6, 1));
        sidePanel.setBackground(Color.BLACK);

        // Create and add buttons to the side panel
        JButton nuevaPartidaButton = new JButton("Nueva Partida");
        JButton configuracionButton = new JButton("Configuración");
        JButton historialButton = new JButton("Historial");
        JButton informacionButton = new JButton("Información");
        JButton salirButton = new JButton("Salir");
        
        // Set button styles
        nuevaPartidaButton.setBackground(Color.BLACK);
        nuevaPartidaButton.setForeground(Color.WHITE);
        nuevaPartidaButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        configuracionButton.setBackground(Color.BLACK);
        configuracionButton.setForeground(Color.WHITE);
        configuracionButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        historialButton.setBackground(Color.BLACK);
        historialButton.setForeground(Color.WHITE);
        historialButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        informacionButton.setBackground(Color.BLACK);
        informacionButton.setForeground(Color.WHITE);
        informacionButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        salirButton.setBackground(Color.BLACK);
        salirButton.setForeground(Color.WHITE);
        salirButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        
        // Adjust button heights
        nuevaPartidaButton.setPreferredSize(buttonSize);
        configuracionButton.setPreferredSize(buttonSize);
        historialButton.setPreferredSize(buttonSize);
        informacionButton.setPreferredSize(buttonSize);
        salirButton.setPreferredSize(buttonSize);
        rotarFichaButton.setPreferredSize(buttonSize);
        cambiarFichaButton.setPreferredSize(buttonSize);

        // Add buttons to the side panel
        sidePanel.add(nuevaPartidaButton);
        sidePanel.add(configuracionButton);
        sidePanel.add(historialButton);
        sidePanel.add(informacionButton);
        sidePanel.add(salirButton);

        // Placeholder for the time bar
        JProgressBar timeBar = new JProgressBar();
        timeBar.setValue(50); // Placeholder value
        sidePanel.add(timeBar);

        // Add the side panel to the container panel on the left
        containerPanel.add(sidePanel, BorderLayout.WEST);

        // Set the background color of the main panel to black
        mainPanel.setBackground(Color.BLACK);

        // Add the main panel to the container panel
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // Add the preview main panel to the container panel on the right
        containerPanel.add(previewMainPanel, BorderLayout.EAST);

        // Create a top panel for the menu bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);

        // Add the "Menu" label
        JLabel menuLabel = new JLabel("Menu");
        menuLabel.setForeground(Color.WHITE);
        topPanel.add(menuLabel);

        // Add icons to the top panel (same icons as buttons)
        JButton icon1Button = new JButton(new ImageIcon("assets/iconoNuevaPartida.jpg"));
        JButton icon2Button = new JButton(new ImageIcon("assets/iconoConfiguracion.jpg"));
        JButton icon3Button = new JButton(new ImageIcon("assets/iconoHistorial.jpg"));
        JButton icon4Button = new JButton(new ImageIcon("assets/iconoInformacion.jpg"));
        JButton icon5Button = new JButton(new ImageIcon("assets/iconoSalir.jpg"));

        // Make the top panel buttons background match the top panel
        icon1Button.setBackground(Color.BLACK);
        icon2Button.setBackground(Color.BLACK);
        icon3Button.setBackground(Color.BLACK);
        icon4Button.setBackground(Color.BLACK);
        icon5Button.setBackground(Color.BLACK);

        // Remove button borders for a flat look
        icon1Button.setBorderPainted(false);
        icon2Button.setBorderPainted(false);
        icon3Button.setBorderPainted(false);
        icon4Button.setBorderPainted(false);
        icon5Button.setBorderPainted(false);

        // Add buttons to the top panel
        topPanel.add(icon1Button);
        topPanel.add(icon2Button);
        topPanel.add(icon3Button);
        topPanel.add(icon4Button);
        topPanel.add(icon5Button);
        
        // Add action listeners to exit the application
        salirButton.addActionListener(e -> System.exit(0));
        icon5Button.addActionListener(e -> System.exit(0));
        
        // ActionListener to show information window
        ActionListener infoActionListener = e -> showInfoWindow();

        // Add action listeners to show the information window
        informacionButton.addActionListener(infoActionListener);
        icon4Button.addActionListener(infoActionListener);

        // Add the top panel to the container panel
        containerPanel.add(topPanel, BorderLayout.NORTH);

        // Add the container panel to the frame
        frame.add(containerPanel);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800); // Set the initial frame size
        frame.setMinimumSize(new Dimension(1200, 800)); // Set minimum size
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tauler.requestFocusInWindow();
            }
        });
    }
    
     private static void showInfoWindow() {
        JFrame infoFrame = new JFrame("Información del Juego");
        infoFrame.setSize(500, 400);
        infoFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        try (BufferedReader br = new BufferedReader(new FileReader("assets/info.txt"))) {
            textArea.read(br, null);
        } catch (IOException e) {
            textArea.setText("No se pudo cargar la información del juego.");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        infoFrame.add(scrollPane);
        infoFrame.setVisible(true);
    }
}