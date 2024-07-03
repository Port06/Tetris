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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//En esta clase ocure toda la lógica del menu principal y del menu de la partida
//asi como las funcionalidades básicas relacionadas con lo menus en la interacción
//de una partida

public class Tetris {
    
    private static JFrame frame;
    private static TetrisGame tetrisGame;
    private static GameMenu gameMenu;

    private static Casella casella;
    private static JPanel gamePanel;
    private static JTextField playerNameField;
    private static JTextField scoreField;
    private static JProgressBar timeBar;
    private static Timer gameTimer;
    private static JPanel sidePanel;
    private static JPanel topPanel;
    
    private static List<AbstractButton> buttonsAndIcons;
    private static boolean isGameActive = false;
    
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
    
    private static JMenuItem nuevaPartidaItem;
    private static JMenuItem configuracionItem;
    private static JMenuItem historialItem;
    private static JMenuItem informacionItem;
    private static JMenuItem salirItem;
    
    private static boolean isInfoWindowOpen = false;



    public static void main(String[] args) {
        
        //Inicio del programa
        SwingUtilities.invokeLater(Tetris::createAndShowGUI);
        tetrisGame = new TetrisGame();
        gameMenu = new GameMenu();
        Rectangle2D.Float initialRect = new Rectangle2D.Float(0, 0, 20, 20);
        casella = new Casella(initialRect, false);
    }
    
    //Primeramente creamos los paneles 1 a 1 asi como el menu principal
    //para tener la base del programa
    private static void createAndShowGUI() {
        
        //Se crea el frame
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

        //Inicializar el menú
        showMainMenu();

        frame.setVisible(true);
    }

    private static void showMainMenu() {
        
        //Initializacion de los botones
        initializeButtonsAndIcons();
        
        //Habilitacion de los botones
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

        //Creacion del panel izquierdo con los botones
        sidePanel = createSidePanel();

        //Creacion del panel superior con los iconos
        topPanel = createTopPanel();

        mainMenuPanel.add(topPanel, BorderLayout.NORTH);
        mainMenuPanel.add(sidePanel, BorderLayout.WEST);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }

    //Initializacion de los botones e iconos
    private static void initializeButtonsAndIcons() {
        
        //Botones
        nuevaPartidaButton = new JButton("Nueva Partida");
        configuracionButton = new JButton("Configuración");
        historialButton = new JButton("Historial");
        informacionButton = new JButton("Información");
        salirButton = new JButton("Salir");
        
        //Iconos
        nuevaPartidaIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoNuevaPartida.jpg")));
        configuracionIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoConfiguracion.jpg")));
        historialIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoHistorial.jpg")));
        informacionIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoInformacion.jpg")));
        salirIconButton = new JButton(new ImageIcon(Tetris.class.getResource("/iconoSalir.jpg")));
        
        //Items
        nuevaPartidaItem = new JMenuItem("Nueva Partida");
        configuracionItem = new JMenuItem("Configuración");
        historialItem = new JMenuItem("Historial");
        informacionItem = new JMenuItem("Información");
        salirItem = new JMenuItem("Salir");
        
        

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
        
        buttonsAndIcons.add(nuevaPartidaItem);
        buttonsAndIcons.add(configuracionItem);
        buttonsAndIcons.add(historialItem);
        buttonsAndIcons.add(informacionItem);
        buttonsAndIcons.add(salirItem);
    }
    
    //Metodo que permite activar y desactivar los botones que lo deberian permitir
    //esto exceptua el boton y icono de salir
    private static void setButtonsAndIconsEnabled(boolean enabled) {
        for (AbstractButton  button : buttonsAndIcons) {
            if (button == null) {
                System.err.println("Found a null button in buttonsAndIcons list.");
                continue;
            }
            if ("Salir".equals(button.getText()) || button.getIcon() == salirIconButton.getIcon()) {
                button.setEnabled(true);
            } else {
                button.setEnabled(enabled);
                if (!enabled) {
                    button.addActionListener(e -> {
                        if (isGameActive) {
                            JOptionPane.showMessageDialog(frame, "¡Espera a que acabe la partida antes!", "Partida en Curso", JOptionPane.WARNING_MESSAGE);
                        }
                    });
                }
            }
        }
    }
    
    
    //Initializaciones de los paneles con botones e iconos a continuacion
    
    //Initializacion del panel de la izquierda
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
    
    //Initializacion del panel superior
    private static JPanel createTopPanel() {
        
        //Initializacion Iconos
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

        configureIconButton(nuevaPartidaIconButton);
        configureIconButton(configuracionIconButton);
        configureIconButton(historialIconButton);
        configureIconButton(informacionIconButton);
        configureIconButton(salirIconButton);

        topPanel.add(nuevaPartidaIconButton);
        topPanel.add(configuracionIconButton);
        topPanel.add(historialIconButton);
        topPanel.add(informacionIconButton);
        topPanel.add(salirIconButton);

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

        salirIconButton.addActionListener(e -> System.exit(0));

        informacionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });

        historialIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showGameHistoryWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        //Initializacion menu desplegable
        JPopupMenu dropdownMenu = new JPopupMenu();

        dropdownMenu.add(nuevaPartidaItem);
        dropdownMenu.add(configuracionItem);
        dropdownMenu.add(historialItem);
        dropdownMenu.add(informacionItem);
        dropdownMenu.add(salirItem);

        nuevaPartidaItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });

        configuracionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });

        salirItem.addActionListener(e -> System.exit(0));

        informacionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });

        historialItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            showGameHistoryWindow();
            setButtonsAndIconsEnabled(true);
        });

        // Mostrar el menú desplegable al hacer clic en el label
        menuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dropdownMenu.show(menuLabel, e.getX(), e.getY());
            }
        });

        return topPanel;
    }
    
    
    
    
    
    //Una vez hemos creado todos los paneles del programa, no encargamos de la
    //logica que requiere de los elementos del menu para funcionar correctamente
    //Estos incluyen iniciar una partida o bien el temporizador de las partidas

    
    
    //Estandarizacion de los botones
    private static void configureButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setPreferredSize(new Dimension(100, 100));
    }
    
    //Estandarizacion de los iconos
    private static void configureIconButton(JButton button) {
        button.setPreferredSize(new Dimension(50, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
    }
    
    //A partir de aqui hay metodos que requieren del acceso a las interfaces
    //para poder funcionar, pero que estan relacionado en cierta parte con la lógica
    //del juego
    
    //Metodo que pide al usuario su nombre para entrar en patida
    private static void promptForPlayerName() {
        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);

        if (playerName != null && !playerName.trim().isEmpty()) {
            if (playerName.length() > tetrisGame.getPlayerNameMaxLength()) {
                playerName = playerName.substring(0, tetrisGame.getPlayerNameMaxLength());  // Truncate the name if it exceeds the max length
            }
            tetrisGame.setPlayerName(playerName);
            gameMenu.startGame();
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
        
        //Definir la partida como acabada
        isGameActive = false;
        
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
        JOptionPane.showMessageDialog(frame, "Time's up! Your score: " + tetrisGame.getPlayerScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
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
    
    //Metodo que muestra el historial de las partidas
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
                //Hacer nada
                break;
        }
    }
    
    //Metodo que muestra la configuracion en el que se modfica los
    //valores a traves de los FieldText
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

                //Se aplican los cambios realizados
                tetrisGame.setRemoveCellCost(puntCasillasEliminadas);
                tetrisGame.setRotateFormScoreCost(puntRotarForma);
                tetrisGame.setChangeFormCost(puntNuevaForma);
                casella.setOcuppiedCellTexture(imgCasillasFormas);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor ingrese valores válidos para las puntuaciones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Metodo para la configuracion del tiempo
    private static void showModifyGameTimeConfiguration() {
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
                break; //Se cancela el cambio
            }
        }
    }
}