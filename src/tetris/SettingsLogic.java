package tetris;

//Classe que se encarga de realizar la acciones de las


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.*;
import java.util.List;

//opciones del menu. Esto incluye la interfaz y lógica
//en question necesaria

public class SettingsLogic {
    
    //Atributos necessarios
    private static JFrame frame;
    private static List<AbstractButton> buttonsAndIcons;
    private static TetrisGame tetrisGame;
    private static GameMenu gameMenu;
    private static Casella casella;
    private static Tetris tetris;
    private static GameIO gameIO;
    

    
    // Constructor
    public SettingsLogic(JFrame frame, List<AbstractButton> buttonsAndIcons, TetrisGame tetrisGame, GameMenu gameMenu, GameIO gameIO) {
        this.frame = frame;
        this.buttonsAndIcons = buttonsAndIcons;
        this.tetrisGame = tetrisGame;
        this.gameMenu = gameMenu;
        this.gameIO = gameIO;
    }
    
    //Metodo que initializa la variable tetris
    public void setTetris(Tetris tetris) {
        this.tetris = tetris;
    }
    
    
    // Estandarizacion de los botones
    public static void configureButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setPreferredSize(new Dimension(100, 100));
    }

    // Estandarizacion de los iconos
    public static void configureIconButton(JButton button) {
        button.setPreferredSize(new Dimension(50, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
    }

    // Metodo que pide al usuario su nombre para entrar en partida
    public void promptForPlayerName() {
        JFrame promptFrame = new JFrame("Player Name");
        promptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        promptFrame.setSize(300, 120);
        promptFrame.setLocationRelativeTo(frame);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Enter your name:");
        JTextField textField = new JTextField(20);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        promptFrame.add(panel);

        okButton.addActionListener(e -> {
            String playerName = textField.getText().trim();
            if (!playerName.isEmpty()) {
                if (playerName.length() > tetrisGame.getPlayerNameMaxLength()) {
                    playerName = playerName.substring(0, tetrisGame.getPlayerNameMaxLength());  // Truncate the name if it exceeds the max length
                }
                tetrisGame.setPlayerName(playerName);
                gameMenu.startGame();
                tetris.setButtonsAndIconsEnabled(true);
                promptFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(promptFrame, "Debes introducir un nombre!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            promptFrame.dispose();
            tetris.setButtonsAndIconsEnabled(true);
        });

        promptFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        tetris.setButtonsAndIconsEnabled(false);
        promptFrame.setVisible(true);
    }

    // Metodo que se encarga de la pestaña de informacion
    public void showInfoWindow() {
        if (tetris.getIsInfoWindowOpen()) {
            return;
        }

        tetris.setIsInfoWindowOpen(true);
        JFrame infoFrame = new JFrame("Información del Juego");
        infoFrame.setSize(500, 400);
        infoFrame.setLocationRelativeTo(frame);

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

        infoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setIsInfoWindowOpen(false);
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        tetris.setButtonsAndIconsEnabled(false);
        infoFrame.setVisible(true);
    }

    // Metodo que muestra el historial de las partidas
    public void showGameHistoryWindow() {

        JFrame historyFrame = new JFrame("Game History");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(600, 500);
        historyFrame.setLocationRelativeTo(frame);

        JTextArea historyTextArea = new JTextArea(10, 30);
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(true);

        StringBuilder historyText = new StringBuilder("Game History:\n");
        for (Game game : gameIO.getCompletedGames()) {
            historyText.append(game.toString()).append("\n");
        }
        historyTextArea.setText(historyText.toString());

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        historyFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        historyFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        historyFrame.setVisible(true);
    }

    // Metodo que se encarga de la logica del menu de configuracion
    public void openConfigurationWindow() {
        JFrame configFrame = new JFrame("CONFIGURACIÓN");
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setSize(300, 150);
        configFrame.setLocationRelativeTo(frame);
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JButton specificGameConfigButton = new JButton("Configuración específica juego");
        JButton modifyGameTimeButton = new JButton("Modificar tiempo partida");
        JButton nothingButton = new JButton("Nada");

        panel.add(specificGameConfigButton);
        panel.add(modifyGameTimeButton);
        panel.add(nothingButton);

        specificGameConfigButton.addActionListener(e -> {
            showSpecificGameConfiguration();
            configFrame.dispose();
        });

        modifyGameTimeButton.addActionListener(e -> {
            showModifyGameTimeConfiguration();
            configFrame.dispose();
        });
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        tetris.setButtonsAndIconsEnabled(false);

        nothingButton.addActionListener(e -> {
            tetris.setButtonsAndIconsEnabled(true);
            configFrame.dispose();
        });

        configFrame.add(panel);
        configFrame.setVisible(true);
    }

    // Metodo que muestra la configuracion en el que se modifica los valores a través de los FieldText
    public void showSpecificGameConfiguration() {
        JFrame configFrame = new JFrame("CONFIGURACIÓN ESPECÍFICA JUEGO");
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setSize(400, 400);
        configFrame.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("     Puntuación Casillas Formas Eliminadas:"));
        JTextField puntCasillasEliminadasField = new JTextField("" + tetrisGame.getRemoveCellCost());
        panel.add(puntCasillasEliminadasField);

        panel.add(new JLabel("     Puntuación Rotar Forma:"));
        JTextField puntRotarFormaField = new JTextField("" + tetrisGame.getRotateFormScoreCost());
        panel.add(puntRotarFormaField);

        panel.add(new JLabel("     Puntuación Nueva Forma:"));
        JTextField puntNuevaFormaField = new JTextField("" + tetrisGame.getChangeFormCost());
        panel.add(puntNuevaFormaField);

        panel.add(new JLabel("     Imagen Casillas Formas:"));
        JTextField imgCasillasFormasField = new JTextField("" + casella.getOcuppiedCellTexture());
        panel.add(imgCasillasFormasField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        okButton.addActionListener(e -> {
            try {
                int puntCasillasEliminadas = Integer.parseInt(puntCasillasEliminadasField.getText());
                int puntRotarForma = Integer.parseInt(puntRotarFormaField.getText());
                int puntNuevaForma = Integer.parseInt(puntNuevaFormaField.getText());
                String imgCasillasFormas = "/" + imgCasillasFormasField.getText() + ".jpg";

                // Se aplican los cambios realizados
                tetrisGame.setRemoveCellCost(puntCasillasEliminadas);
                tetrisGame.setRotateFormScoreCost(puntRotarForma);
                tetrisGame.setChangeFormCost(puntNuevaForma);
                casella.setOcuppiedCellTexture(imgCasillasFormas);
                configFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(configFrame, "Por favor ingrese valores válidos para las puntuaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        tetris.setButtonsAndIconsEnabled(false);

        cancelButton.addActionListener(e -> {
            tetris.setButtonsAndIconsEnabled(true);
            configFrame.dispose();
        });

        configFrame.add(panel);
        configFrame.setVisible(true);
    }

    // Metodo para la configuración del tiempo
    public void showModifyGameTimeConfiguration() {
        JFrame configFrame = new JFrame("MODIFICAR TIEMPO PARTIDA");
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setSize(300, 150);
        configFrame.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tiempo de Partida:"));
        JTextField tiempoPartidaField = new JTextField("" + tetrisGame.getTotalGameTime());
        panel.add(tiempoPartidaField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        okButton.addActionListener(e -> {
            try {
                int tiempoPartida = Integer.parseInt(tiempoPartidaField.getText());
                if (tiempoPartida > 0) {
                    tetrisGame.setTotalGameTime(tiempoPartida);
                    configFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(configFrame, "El tiempo de partida debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(configFrame, "Por favor ingrese un valor válido para el tiempo de partida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setButtonsAndIconsEnabled(true);
            }
        });

        tetris.setButtonsAndIconsEnabled(false);

        cancelButton.addActionListener(e -> {
            tetris.setButtonsAndIconsEnabled(true);
            configFrame.dispose();
        });

        configFrame.add(panel);
        configFrame.setVisible(true);
    }  
}
