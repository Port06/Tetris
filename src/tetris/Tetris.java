package tetris;

import javax.swing.*;
import java.awt.*;
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
    
    //Definición de los paneles y varibles de las otras classes para 
    //poder estructurar la ventana
    public static JFrame frame;
    
    private static TetrisGame tetrisGame;
    private static GameMenu gameMenu;
    private static Casella casella;
    private static SettingsLogic settingsLogic;
    
    private static JPanel gamePanel;
    private static JTextField scoreField;
    private static JProgressBar timeBar;
    private static Timer gameTimer;
    private static JPanel sidePanel;
    private static JPanel topPanel;
    
    //Fichero de la serializacion para almacenar partidas
    private static final String GAME_DATA_FILE = "assets/partidasTetrisUIB.dat";
    private static List<Game> completedGames = new ArrayList<>();
    
    //Aquí se definen todos los botones de la interfaz
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
    
    //Variables para definir el tamano de la pestana y 
    //mantener un registro de si  se pueden usar los botones del menu
    private static boolean isInfoWindowOpen = false;
    private static final int WINDOWHEIGHT = 800; 
    private static final int WINDOWIDTH = 1200; 
    
    private static List<AbstractButton> buttonsAndIcons;
    private static boolean isGameActive = false;


    public Tetris() {
        
        //Se aplaza la creación del manu tras haber instanciado las classes
        //adecuandamente
        SwingUtilities.invokeLater(Tetris::createAndShowGUI);
        
        
        //Creacion de las instancias de las classes
        tetrisGame = new TetrisGame();
        gameMenu = new GameMenu();
        Rectangle2D.Float initialRect = new Rectangle2D.Float(0, 0, 20, 20);
        casella = new Casella(initialRect, false);
        
    }
    
    //Getters y setters
    // Getters for the private fields
    public List<AbstractButton> getButtonsAndIcons() {
        return buttonsAndIcons;
    }

    public TetrisGame getTetrisGame() {
        return tetrisGame;
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }
    
    public boolean getIsInfoWindowOpen() {
        return isInfoWindowOpen;
    }
    
    public void setIsInfoWindowOpen(boolean isInfoWindowOpen) {
        this.isInfoWindowOpen = isInfoWindowOpen;
    }
    
    
    
    
    //Primeramente creamos los paneles 1 a 1 asi como el menu principal
    //para tener la base del programa
    private static void createAndShowGUI() {
        
        //Se crea el frame
        frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOWIDTH, WINDOWHEIGHT);
        frame.setMinimumSize(new Dimension(WINDOWIDTH, WINDOWHEIGHT));
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

        //Mostrar la ventana
        frame.setVisible(true);
    }

    
    //Metodo que permite crear el menu principal de lproyaecto
    //con la initializacion de los botones y paneles asi como el fondo
    //del menu
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
        
        
        
        //Creación de un arraylist para que se puedan desactivar los botones
        //iconos y items cunado se esta en partida
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
    public static void setButtonsAndIconsEnabled(boolean enabled) {
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

        settingsLogic.configureButton(nuevaPartidaButton);
        settingsLogic.configureButton(configuracionButton);
        settingsLogic.configureButton(historialButton);
        settingsLogic.configureButton(informacionButton);
        settingsLogic.configureButton(salirButton);

        
        //Metodos que llaman la logica de la configuracion
        //y que descativan el uso de otros acciones mientras
        nuevaPartidaButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });
        
        configuracionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        salirButton.addActionListener(e -> System.exit(0));
        
        informacionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        historialButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
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

        settingsLogic.configureIconButton(nuevaPartidaIconButton);
        settingsLogic.configureIconButton(configuracionIconButton);
        settingsLogic.configureIconButton(historialIconButton);
        settingsLogic.configureIconButton(informacionIconButton);
        settingsLogic.configureIconButton(salirIconButton);

        topPanel.add(nuevaPartidaIconButton);
        topPanel.add(configuracionIconButton);
        topPanel.add(historialIconButton);
        topPanel.add(informacionIconButton);
        topPanel.add(salirIconButton);

        
        //Metodos que llaman la logica de la configuracion
        //y que descativan el uso de otros acciones mientras
        nuevaPartidaIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });

        configuracionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });

        salirIconButton.addActionListener(e -> System.exit(0));

        informacionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });

        historialIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
            setButtonsAndIconsEnabled(true);
        });
        
        //Initializacion menu desplegable
        JPopupMenu dropdownMenu = new JPopupMenu();

        dropdownMenu.add(nuevaPartidaItem);
        dropdownMenu.add(configuracionItem);
        dropdownMenu.add(historialItem);
        dropdownMenu.add(informacionItem);
        dropdownMenu.add(salirItem);

        
        //Metodos que llaman la logica de la configuracion
        //y que descativan el uso de otros acciones mientras
        nuevaPartidaItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.promptForPlayerName();
            setButtonsAndIconsEnabled(true);
        });

        configuracionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            setButtonsAndIconsEnabled(true);
        });

        salirItem.addActionListener(e -> System.exit(0));

        informacionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            setButtonsAndIconsEnabled(true);
        });

        historialItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
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
    
    
    
    

    
    //PENDING PENDING
    
    
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
}