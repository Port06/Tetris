package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
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
    private static GameIO gameLoader;
    private static SettingsLogic settingsLogic;
    private static GameIO gameIO;
    
    private static JPanel gamePanel;
    private static JTextField scoreField;
    private static JProgressBar timeBar;
    private static Timer gameTimer;
    private static JPanel sidePanel;
    private static JPanel topPanel;
    
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


    public Tetris(GameIO gameIO, GameMenu gameMenu, TetrisGame tetrisGame) {
        // Create instances of other classes
        this.gameIO = gameIO;
        this.gameMenu = gameMenu;
        this.tetrisGame = tetrisGame;
        Rectangle2D.Float initialRect = new Rectangle2D.Float(0, 0, 20, 20);
        casella = new Casella(initialRect, false);

        // Schedule a job for the event-dispatching thread to create and show the GUI
        SwingUtilities.invokeLater(Tetris::createAndShowGUI);
    }
    
    //Getters y setters
    // Getters for the private fields
    public void setSettingsLogic(SettingsLogic settingsLogic) {
        this.settingsLogic = settingsLogic;
    }
    public List<AbstractButton> getButtonsAndIcons() {
        return buttonsAndIcons;
    }
    
    public static JPanel getSidePanel() {
        return sidePanel;
    }
    
    public static JPanel getTopPanel() {
        return topPanel;
    }
    
    public static JTextField getScoreField() {
        return scoreField;
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
    
    public boolean getIsGameActive() {
        return isGameActive;
    }
    
    public void setIsGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;
    }
    
    public void setIsInfoWindowOpen(boolean isInfoWindowOpen) {
        this.isInfoWindowOpen = isInfoWindowOpen;
    }
    
    
    //A partir de aquí se genera los paneles con los botones, 
    //iconos e items necesarios
    
    
    //Primeramente creamos los paneles 1 a 1 asi como el menu principal
    //para tener la base del programa
    private static void createAndShowGUI() {
        
        //Se crea el frame
        frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOWIDTH, WINDOWHEIGHT);
        frame.setMinimumSize(new Dimension(WINDOWIDTH, WINDOWHEIGHT));
        frame.setLocationRelativeTo(null);
        
        //Cargar las partidas del historial del fichero serializado
        gameIO.loadGameHistory();

        //Inicializar el menú
        showMainMenu();

        //Mostrar la ventana
        frame.setVisible(true);
    }

    
    //Metodo que permite crear el menu principal de lproyaecto
    //con la initializacion de los botones y paneles asi como el fondo
    //del menu
    public static void showMainMenu() {
        
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
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        configuracionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
                
        historialButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        informacionButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        salirButton.addActionListener(e -> System.exit(0));

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
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });

        configuracionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        historialIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });

        informacionIconButton.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        salirIconButton.addActionListener(e -> System.exit(0));
        
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
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });

        configuracionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.openConfigurationWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        historialItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showGameHistoryWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });

        informacionItem.addActionListener(e -> {
            setButtonsAndIconsEnabled(false);
            settingsLogic.showInfoWindow();
            //No se deben reactivar los botones
            //porque queremos que esten desactivados 
        });
        
        salirItem.addActionListener(e -> System.exit(0));

        // Mostrar el menú desplegable al hacer clic en el label
        menuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dropdownMenu.show(menuLabel, e.getX(), e.getY());
            }
        });

        return topPanel;
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
}