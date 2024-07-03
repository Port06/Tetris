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
    

    
    // Constructor
    public SettingsLogic(JFrame frame, List<AbstractButton> buttonsAndIcons, TetrisGame tetrisGame, GameMenu gameMenu) {
        this.frame = frame;
        this.buttonsAndIcons = buttonsAndIcons;
        this.tetrisGame = tetrisGame;
        this.gameMenu = gameMenu;
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
        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);

        if (playerName != null && !playerName.trim().isEmpty()) {
            if (playerName.length() > tetrisGame.getPlayerNameMaxLength()) {
                playerName = playerName.substring(0, tetrisGame.getPlayerNameMaxLength());  // Truncate the name if it exceeds the max length
            }
            tetrisGame.setPlayerName(playerName);
            gameMenu.startGame();
            tetris.setButtonsAndIconsEnabled(true);
        } else {
            if (playerName == null) {  // La caja de dialogo es cerrada
                // NO se hace nada
            } else {  // La caja de dialogo esta vacia pero se intenta iniciar partida
                JOptionPane.showMessageDialog(frame, "Debes intrudicr un nombre!", "Error", JOptionPane.ERROR_MESSAGE);
                promptForPlayerName();  // Se le vuelve a pedir un nombre
            }
        }
    }

    // Metodo que se encarga de la pestaña de informacion
    public void showInfoWindow() {
        if (tetris.getIsInfoWindowOpen()) {
            return;
        }

        tetris.setIsInfoWindowOpen(true);
        JDialog infoDialog = new JDialog(frame, "Información del Juego", true);
        infoDialog.setSize(500, 400);
        infoDialog.setLocationRelativeTo(frame);

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

        infoDialog.add(scrollPane);

        infoDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tetris.setIsInfoWindowOpen(false);
            }
        });

        infoDialog.setVisible(true);
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
        for (Game game : tetris.getCompletedGames()) {
            historyText.append(game.toString()).append("\n");
        }
        historyTextArea.setText(historyText.toString());

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        historyFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        historyFrame.setVisible(true);
    }

    // Metodo que se encarga de la logica del menu de configuracion
    public void openConfigurationWindow() {
        String[] options = {"Configuración específica juego", "Modificar tiempo partida", "Nada"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "¿QUÉ DESEAS REALIZAR?",
                "CONFIGURACIÓN",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                showSpecificGameConfiguration();
                break;
            case 1:
                showModifyGameTimeConfiguration();
                break;
            case 2:
            default:
                // Hacer nada
                break;
        }
    }

    // Metodo que muestra la configuracion en el que se modifica los valores a través de los FieldText
    public void showSpecificGameConfiguration() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Puntuación Casillas Formas Eliminadas:"));
        JTextField puntCasillasEliminadasField = new JTextField("" + tetrisGame.getRemoveCellCost());
        panel.add(puntCasillasEliminadasField);

        panel.add(new JLabel("Puntuación Rotar Forma:"));
        JTextField puntRotarFormaField = new JTextField("" + tetrisGame.getRotateFormScoreCost());
        panel.add(puntRotarFormaField);

        panel.add(new JLabel("Puntuación Nueva Forma:"));
        JTextField puntNuevaFormaField = new JTextField("" + tetrisGame.getChangeFormCost());
        panel.add(puntNuevaFormaField);

        panel.add(new JLabel("Imagen Casillas Formas:"));
        JTextField imgCasillasFormasField = new JTextField("" + casella.getOcuppiedCellTexture());
        panel.add(imgCasillasFormasField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "CONFIGURACIÓN ESPECÍFICA JUEGO", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
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
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor ingrese valores válidos para las puntuaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo para la configuración del tiempo
    public void showModifyGameTimeConfiguration() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Tiempo de Partida:"));
        JTextField tiempoPartidaField = new JTextField("" + tetrisGame.getTotalGameTime());
        panel.add(tiempoPartidaField);

        boolean validInput = false;

        while (!validInput) {
            int result = JOptionPane.showConfirmDialog(frame, panel, "MODIFICAR TIEMPO PARTIDA", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int tiempoPartida = Integer.parseInt(tiempoPartidaField.getText());
                    if (tiempoPartida > 0) {
                        tetrisGame.setTotalGameTime(tiempoPartida);
                        validInput = true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "El tiempo de partida debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Por favor ingrese un valor válido para el tiempo de partida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break; // Se cancela el cambio
            }
        }
    }
    
}
