package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//En esta clase ocure toda la lógica del menu principal y del menu de la partida
//asi como las funcionalidades básicas relacionadas con lo menus en la interacción
//de una partida
public class Tetris {
    
    private static JFrame frame;
    private static TetrisGame tetrisGame;
    private static PreviewPanel previewPanel;
    private static Tauler tauler;
    private static Tetris tetris;
    private static Casella casella;
    private static JPanel gamePanel;
    private static JTextField playerNameField;
    private static JTextField scoreField;
    private static JProgressBar timeBar;
    private static Timer gameTimer;
    private static JPanel sidePanel;
    private static JPanel topPanel;
    
    private static List<JButton> buttonsAndIcons;
    
    private static final int MAX_NAME_LENGTH = 25;  //Aquí se define la longitud
                                                    //máxima del nombre del jugador
    
    //Fichero de la serializacion
    private static final String GAME_DATA_FILE = "assets/partidasTetrisUIB.dat";
    private static List<Game> completedGames = new ArrayList<>();
    
    // Declare the icon buttons and buttons at the class level
    private static JButton nuevaPartidaIconButton;
    private static JButton configuracionIconButton;
    private static JButton historialIconButton;
    private static JButton informacionIconButton;
    private static JButton salirIconButton;
    
    private static JButton nuevaPartidaButton;
    private static JButton configuracionButton;
    private static JButton historialButton;
    private static JButton informacionButton;
    private static JButton salirButton;
    
    private static boolean isInfoWindowOpen = false;



    public static void main(String[] args) {
        
        //Inicio del programa
        SwingUtilities.invokeLater(Tetris::createAndShowGUI);
        tetrisGame = new TetrisGame();
        Rectangle2D.Float initialRect = new Rectangle2D.Float(0, 0, 20, 20);
        casella = new Casella(initialRect, false);
    }
    
    //Primeramente creamos los paneles 1 a 1 asi como el menu principal
    //para tener la base del programa
    private static void createAndShowGUI() {
        // Crear el frame
        frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setLocationRelativeTo(null);

        // Inicializar la lista de juegos completados desde el archivo
        File gameDataFile = new File(GAME_DATA_FILE);
        if (gameDataFile.exists() && gameDataFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GAME_DATA_FILE))) {
                completedGames = (List<Game>) ois.readObject();
            } catch (EOFException e) {
                System.out.println("Reached end of file unexpectedly.");
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading game data. The file might be corrupt or incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException e) {
                System.out.println("Game data file not found, starting fresh.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading game data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Game data file not found or empty, starting fresh.");
        }

        // Inicializar el menú
        showMainMenu();

        // Hacer visible la pestaña
        frame.setVisible(true);
    }

    private static void showMainMenu() {
        
        // Initialize the buttons and icons list after creating them
        initializeButtonsAndIcons();
        
        //Set buttons to enabled
        setButtonsAndIconsEnabled(true);
        
        //Creamos el panel de menu principal
        JPanel mainMenuPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon(Tetris.class.getResource("/TETRIS_UIB.jpg")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainMenuPanel.setBackground(Color.BLACK);

        // Create side panel for the menu and buttons
        sidePanel = createSidePanel();

        // Create top panel for the icons and menu label
        topPanel = createTopPanel();

        mainMenuPanel.add(topPanel, BorderLayout.NORTH);
        mainMenuPanel.add(sidePanel, BorderLayout.WEST);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }

     private static void initializeButtonsAndIcons() {
        nuevaPartidaButton = new JButton("Nueva Partida");
        configuracionButton = new JButton("Configuración");
        historialButton = new JButton("Historial");
        informacionButton = new JButton("Información");
        salirButton = new JButton("Salir");

        nuevaPartidaIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoNuevaPartida.jpg")));
        configuracionIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoConfiguracion.jpg")));
        historialIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoHistorial.jpg")));
        informacionIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoInformacion.jpg")));
        salirIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoSalir.jpg")));

        buttonsAndIcons = new ArrayList<>();

        buttonsAndIcons.add(nuevaPartidaButton);
        buttonsAndIcons.add(configuracionButton);
        buttonsAndIcons.add(historialButton);
        buttonsAndIcons.add(informacionButton);
        buttonsAndIcons.add(salirButton);

        buttonsAndIcons.add(nuevaPartidaIconButton);
        buttonsAndIcons.add(configuracionIconButton);
        buttonsAndIcons.add(historialIconButton);
        buttonsAndIcons.add(informacionIconButton);
        buttonsAndIcons.add(salirIconButton);
    }
    
    //Metodo que permite activar y desactivar los botones que lo deberian permitir
    //esto exceptua el boton y icono de salir
    private static void setButtonsAndIconsEnabled(boolean enabled) {
        for (JButton button : buttonsAndIcons) {
            if (button == null) {
                System.err.println("Found a null button in buttonsAndIcons list.");
                continue;
            }
            // Check for "Salir" button or icon and skip enabling/disabling them
            if ("Salir".equals(button.getText()) || button.getIcon() == salirIconButton.getIcon()) {
                button.setEnabled(true); // Ensure "Salir" button is always enabled
            } else {
                button.setEnabled(enabled);
            }
        }
    }

    private static JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(6, 1));
        sidePanel.setBackground(Color.BLACK);

        configureButton(nuevaPartidaButton);
        configureButton(configuracionButton);
        configureButton(historialButton);
        configureButton(informacionButton);
        configureButton(salirButton);

        nuevaPartidaButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });
        
        configuracionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        salirButton.addActionListener(e -> System.exit(0));
        
        informacionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        historialButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showGameHistoryWindow();
            setButtonsAndIconsEnabled(true);
        });

        sidePanel.add(nuevaPartidaButton);
        sidePanel.add(configuracionButton);
        sidePanel.add(historialButton);
        sidePanel.add(informacionButton);
        sidePanel.add(salirButton);

        return sidePanel;
    }

    private static JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);

        JLabel menuLabel = new JLabel("Menu");
        menuLabel.setForeground(Color.WHITE);
        topPanel.add(menuLabel);

        nuevaPartidaIconButton.setBackground(Color.BLACK);
        configuracionIconButton.setBackground(Color.BLACK);
        historialIconButton.setBackground(Color.BLACK);
        informacionIconButton.setBackground(Color.BLACK);
        salirIconButton.setBackground(Color.BLACK);

        nuevaPartidaIconButton.setBorderPainted(false);
        configuracionIconButton.setBorderPainted(false);
        historialIconButton.setBorderPainted(false);
        informacionIconButton.setBorderPainted(false);
        salirIconButton.setBorderPainted(false);
        nuevaPartidaIconButton.setFocusPainted(false);
        configuracionIconButton.setFocusPainted(false);
        historialIconButton.setFocusPainted(false);
        informacionIconButton.setFocusPainted(false);
        salirIconButton.setFocusPainted(false);

        nuevaPartidaIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });
        configuracionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });
        informacionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });
        salirIconButton.addActionListener(e -> System.exit(0));

        topPanel.add(nuevaPartidaIconButton);
        topPanel.add(configuracionIconButton);
        topPanel.add(historialIconButton);
        topPanel.add(informacionIconButton);
        topPanel.add(salirIconButton);

        return topPanel;
    }
    
    //Una vez hemos creado todos los paneles del programa, no encargamos de la
    //logica que requiere de los elementos del menu para funcionar correctamente
    //Estos incluyen iniciar una partida o bien el temporizador de las partidas

    private static void startGame() {
        
        // Disable all buttons except exit
         SwingUtilities.invokeLater(() -> setButtonsAndIconsEnabled(false));
              
        // Initializamos los componentes del juego restante
        previewPanel = new PreviewPanel(tetrisGame);
        tauler = new Tauler(previewPanel, tetrisGame, tetris);

        tetrisGame.setPreviewPanel(previewPanel);
        tetrisGame.setTauler(tauler);

        // Initialize the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 100));

        playerNameField = new JTextField("Name: " + tetrisGame.getPlayerName());
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
        scoreField.setFont(new Font("Arial", Font.PLAIN, 18)); //Cambiar tamano

        //Definir las dimensiones de los contenedores
        Dimension fieldSize = new Dimension(200, 40); //Ajustar altura
        playerNameField.setPreferredSize(fieldSize);
        scoreField.setPreferredSize(fieldSize);

        //Se initializa la barra de tiempo al valor de tiempo especificado en la configuracion
        timeBar = new JProgressBar(0, tetrisGame.getTotalGameTime());
        timeBar.setValue(tetrisGame.getTotalGameTime());
        timeBar.setForeground(Color.GREEN); // Change progress bar color here

        //Definir el tamano de la barra de tiempo
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

        JPanel previewMainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image fondoImage = new ImageIcon(Tetris.class.getResource("/FONDO.jpg")).getImage();
                g.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
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
                tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getRotateFormScoreCost()); //Se penaliza al jugador
                scoreField.setText("Score: " + tetrisGame.getPlayerScore());
                previewPanel.repaint();
            }
        });

        cambiarFichaButton.addActionListener(e -> {
            tetrisGame.updatePiece();
            tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getChangeFormCost()); //Se penaliza el jugador
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

        
        //Empiza el contador de la partida para que el jugador tenga un tiempo
        //limitado por partida
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

        gameTimer.start();
    }
    
    private static void configureButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setPreferredSize(new Dimension(100, 100));
    }

    private static void promptForPlayerName() {
        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);

        if (playerName != null && !playerName.trim().isEmpty()) {
            if (playerName.length() > MAX_NAME_LENGTH) {
                playerName = playerName.substring(0, MAX_NAME_LENGTH);  // Truncate the name if it exceeds the max length
            }
            tetrisGame.setPlayerName(playerName);
            startGame();
            setButtonsAndIconsEnabled(true);
        } else {
            if (playerName == null) {  //La caja de dialogo es cerrada
                //NO se hace nada
            } else {  //La caja de dialogo esta vacia pero se intenta iniciar partida
                JOptionPane.showMessageDialog(frame, "Debes intrudicr un nombre!", "Error", JOptionPane.ERROR_MESSAGE);
                promptForPlayerName();  //Se le vuelve a pedir un nombre
            }
        }
    }
    
    //Metodo que incrementa la puntuacion al retirar una fila o columna
    public static void increaseScore() {
        tetrisGame.setPlayerScore(tetrisGame.getPlayerScore() + tetrisGame.getRemoveCellCost());
        scoreField.setText("Score: " + tetrisGame.getPlayerScore());
    }
    
    //Metodo que realiza la logica del final de partida
    private static void endGame() {
        
        //Se crea la instancia de una partida para el registro.
        //Esto solo ocurre en caso de que se haya completado una partida.
        //Es decir, si se sale de la partida a medias no se guardara
        Game completedGame = new Game(
            tetrisGame.getPlayerName(),
            LocalDateTime.now(),
            tetrisGame.getTotalGameTime() - tetrisGame.getGameTime(), // Total time played
            tetrisGame.getPlayerScore()
        );
        
        // Agregar el juego completado a la lista
        completedGames.add(completedGame);
        
        //Se serializan las intancias creadas
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GAME_DATA_FILE))) {
            oos.writeObject(completedGames);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving game data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //Se muestra el resultado al usuario
        JOptionPane.showMessageDialog(frame, "Time's up! Your score: 0", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        showMainMenu();
    }
    
    //Metodo que se encarga de la pestana de informacion
    private static void showInfoWindow() {
        if (isInfoWindowOpen) {
            return;
        }

        isInfoWindowOpen = true;
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
                isInfoWindowOpen = false;
            }
        });

        infoDialog.setVisible(true);
    }
    
    private static void showGameHistoryWindow() {
        JFrame historyFrame = new JFrame("Game History");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(600, 500);
        historyFrame.setLocationRelativeTo(frame);

        JTextArea historyTextArea = new JTextArea(10, 30);
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(true);

        StringBuilder historyText = new StringBuilder("Game History:\n");
        for (Game game : completedGames) {
            historyText.append(game.toString()).append("\n");
        }
        historyTextArea.setText(historyText.toString());

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        historyFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        historyFrame.setVisible(true);
    }
   
    //Metodo que se encarga de la logica del menu de configuracion
    private static void openConfigurationWindow() {
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
                // Do nothing
                break;
        }
    }
    
    // Configuration method
    private static void showSpecificGameConfiguration() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Puntuación Casillas Formas Eliminadas:"));
        JTextField puntCasillasEliminadasField = new JTextField("" + tetrisGame.getRemoveCellCost());
        panel.add(puntCasillasEliminadasField);

        panel.add(new JLabel("Puntuación Rotar Forma:"));
        JTextField puntRotarFormaField = new JTextField("" + tetrisGame.getRotateFormScoreCost());
        panel.add(puntRotarFormaField);

        panel.add(new JLabel("Puntuación Nueva Forma:"));
        JTextField puntNuevaFormaField = new JTextField("" + tetrisGame.getChangeFormCost());
        System.out.println(tetrisGame.getChangeFormCost());
        panel.add(puntNuevaFormaField);

        panel.add(new JLabel("Imagen Casillas Formas:"));
        JTextField imgCasillasFormasField = new JTextField("" + casella.getOcuppiedCellTexture());
        System.out.println(casella.getOcuppiedCellTexture());
        panel.add(imgCasillasFormasField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "CONFIGURACIÓN ESPECÍFICA JUEGO", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int puntCasillasEliminadas = Integer.parseInt(puntCasillasEliminadasField.getText());
                int puntRotarForma = Integer.parseInt(puntRotarFormaField.getText());
                int puntNuevaForma = Integer.parseInt(puntNuevaFormaField.getText());
                String imgCasillasFormas = imgCasillasFormasField.getText();

                // Apply the changes
                tetrisGame.setRemoveCellCost(puntCasillasEliminadas);
                tetrisGame.setRotateFormScoreCost(puntRotarForma);
                tetrisGame.setChangeFormCost(puntNuevaForma);
                casella.setOcuppiedCellTexture(imgCasillasFormas);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor ingrese valores válidos para las puntuaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Time configuration method
    private static void showModifyGameTimeConfiguration() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Tiempo de Partida:"));
        JTextField tiempoPartidaField = new JTextField("" + tetrisGame.getTotalGameTime());
        panel.add(tiempoPartidaField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "MODIFICAR TIEMPO PARTIDA", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int tiempoPartida = Integer.parseInt(tiempoPartidaField.getText());
                tetrisGame.setTotalGameTime(tiempoPartida);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor ingrese un valor válido para el tiempo de partida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}