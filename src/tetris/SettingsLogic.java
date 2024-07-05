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
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.*;
import java.util.List;
import java.io.InputStream;
import static tetris.Tetris.setButtonsAndIconsEnabled;

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
    
    // Método que se encarga de la pestaña de información
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
        
        // Establecer el tamaño de fuente del JTextArea
        Font textAreaFont = new Font("Arial", Font.PLAIN, 16); // Cambia 16 al tamaño de fuente deseado
        textArea.setFont(textAreaFont);

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
                setButtonsAndIconsEnabled(true);
            }
        });

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
        
        // Establecer el tamaño de fuente del JTextArea
        Font historyTextAreaFont = new Font("Arial", Font.PLAIN, 16); // Cambia 16 al tamaño de fuente deseado
        historyTextArea.setFont(historyTextAreaFont);

        StringBuilder historyText = new StringBuilder("Game History:\n");
        for (Game game : gameIO.getCompletedGames()) {
            historyText.append(game.toString()).append("\n");
        }
        historyTextArea.setText(historyText.toString());

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        historyFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        historyFrame.setVisible(true);
        
        historyFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setButtonsAndIconsEnabled(true);
            }
        });
    }

    // Método que se encarga de la lógica del menú de configuración
    public void openConfigurationWindow() {
        JFrame configFrame = new JFrame("Configuración");
        configFrame.setSize(300, 200);
        configFrame.setLocationRelativeTo(frame);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel titleLabel = new JLabel("¿QUÉ DESEAS REALIZAR?", JLabel.CENTER);
        panel.add(titleLabel);

        JButton specificGameConfigButton = new JButton("Configuración específica juego");
        specificGameConfigButton.addActionListener(e -> {
        configFrame.dispose();
        showSpecificGameConfiguration();
        });
        panel.add(specificGameConfigButton);

        JButton modifyGameTimeButton = new JButton("Modificar tiempo partida");
        modifyGameTimeButton.addActionListener(e -> {
        configFrame.dispose();
        showModifyGameTimeConfiguration();
        });
        panel.add(modifyGameTimeButton);
        
        JButton nothingButton = new JButton("Nada");
        nothingButton.addActionListener(e -> {
            configFrame.dispose();
            setButtonsAndIconsEnabled(true);
        });
        panel.add(nothingButton);

        configFrame.add(panel);
        configFrame.setVisible(true);
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setButtonsAndIconsEnabled(true);
            }
        });
    }

    // Método que muestra la configuración en el que se modifica los valores a través de los FieldText
    public void showSpecificGameConfiguration() {
        JFrame configFrame = new JFrame("CONFIGURACIÓN ESPECÍFICA JUEGO");
        configFrame.setSize(500, 300);
        configFrame.setLocationRelativeTo(frame);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        // Obtener el texto actual del JTextField
        String text = imgCasillasFormasField.getText();

        // Verificar si el texto tiene al menos 5 caracteres (para eliminar el primero y los últimos cuatro)
        if (text.length() >= 5) {
            // Eliminar el primer carácter y los últimos cuatro caracteres
            String newText = text.substring(1, text.length() - 4);

            // Establecer el nuevo texto en el JTextField
            imgCasillasFormasField.setText(newText);
        } else {
            // Manejar caso donde el texto es demasiado corto para realizar la operación deseada
            System.out.println("El texto es demasiado corto para eliminar el primer y los últimos cuatro caracteres.");
        }
        panel.add(imgCasillasFormasField);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                int puntCasillasEliminadas = Integer.parseInt(puntCasillasEliminadasField.getText());
                int puntRotarForma = Integer.parseInt(puntRotarFormaField.getText());
                int puntNuevaForma = Integer.parseInt(puntNuevaFormaField.getText());
                String imgCasillasFormas = imgCasillasFormasField.getText().trim();
                
                
                // Check if the texture file exists
                String texturePath = "/" + imgCasillasFormas + ".jpg";
                InputStream inputStream = getClass().getResourceAsStream(texturePath);
                if (inputStream == null) {
                    JOptionPane.showMessageDialog(configFrame, "El archivo de textura especificado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; //Se vuelve a pedir al usuario una textura válida
                }
                
                // Se aplican los cambios realizados
                tetrisGame.setRemoveCellCost(puntCasillasEliminadas);
                tetrisGame.setRotateFormScoreCost(puntRotarForma);
                tetrisGame.setChangeFormCost(puntNuevaForma);
                casella.setOcuppiedCellTexture(texturePath);
                setButtonsAndIconsEnabled(true);

                configFrame.dispose(); // Cierra el frame cuando se presiona OK
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(configFrame, "Por favor ingrese valores válidos para las puntuaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
        configFrame.dispose(); // Cierra el frame cuando se presiona Cancelar
        setButtonsAndIconsEnabled(true);
        });
        buttonPanel.add(cancelButton);

        configFrame.add(panel, BorderLayout.CENTER);
        configFrame.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.setVisible(true);
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setButtonsAndIconsEnabled(true);
            }
        });
    }

    // Método para la configuración del tiempo
    public void showModifyGameTimeConfiguration() {
        JFrame configFrame = new JFrame("MODIFICAR TIEMPO PARTIDA");
        configFrame.setSize(300, 150);
        configFrame.setLocationRelativeTo(frame);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Tiempo de Partida:"));
        JTextField tiempoPartidaField = new JTextField("" + tetrisGame.getTotalGameTime());
        panel.add(tiempoPartidaField);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                int tiempoPartida = Integer.parseInt(tiempoPartidaField.getText());
                if (tiempoPartida > 0) {
                    tetrisGame.setTotalGameTime(tiempoPartida);
                    setButtonsAndIconsEnabled(true);
                    configFrame.dispose(); // Closes the frame when OK is pressed
                } else {
                    JOptionPane.showMessageDialog(configFrame, "El tiempo de partida debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(configFrame, "Por favor ingrese un valor válido para el tiempo de partida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> {
        configFrame.dispose(); // Closes the frame when Cancel is pressed
        setButtonsAndIconsEnabled(true);
        });
        buttonPanel.add(cancelButton);

        configFrame.add(panel, BorderLayout.CENTER);
        configFrame.add(buttonPanel, BorderLayout.SOUTH);

        configFrame.setVisible(true);
        
        configFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                setButtonsAndIconsEnabled(true);
            }
        });
    } 
}
