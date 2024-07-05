package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

public class Tauler extends JPanel implements KeyListener {

    public static final int DIMENSIO = 20;
    private static final int MAXIM = 500;
    private static final int COSTAT = MAXIM / DIMENSIO;
    private Casella[][] t;
    private Point mousePosition;
    
    
    //Variables aditionales para acceder a otras clases
    private TetrisGame tetrisGame;
    private PreviewPanel previewPanel;
    private Tetris tetris;
    private GameMenu gameMenu;

    public Tauler(PreviewPanel previewPanel, TetrisGame tetrisGame, Tetris tetris, GameMenu gameMenu) {
        
        this.previewPanel = previewPanel;
        this.tetrisGame = tetrisGame;
        this.tetris = tetris;
        this.gameMenu = gameMenu;
        
        setFocusable(true); //Permitir el uso de las teclas manteniedo el foco
        addKeyListener(this); //Keylistener register
        setFocusTraversalKeysEnabled(false);
        
        //Asegurar-se que el focus se mantiene al abrir la ventana
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
        
        
        //Creacion de las casillas y del tablero
        t = new Casella[DIMENSIO][DIMENSIO];
        int y = 0;

        for (int i = 0; i < DIMENSIO; i++) {
            int x = 0;
            for (int j = 0; j < DIMENSIO; j++) {
                Rectangle2D.Float r = new Rectangle2D.Float(x, y, COSTAT, COSTAT);
                t[i][j] = new Casella(r, false); // Initialize as empty tiles
                x += COSTAT;
            }
            y += COSTAT;
        }
        
        
        //Creacion de los metodos necesarios que utilizan el raton
        //para poder arastrar piezas por el tablero asi como colocarlas
        previewPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMousePosition(e);
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (tetrisGame.getDraggedPiece() != null) {
                    int row = e.getY() / COSTAT;
                    int col = e.getX() / COSTAT;

                    //Se coloca a las casillas mas cercanas (snap to grid)
                    int startX = Math.round((float)col - tetrisGame.getDraggedPiece().getWidth() / 2.0f);
                    int startY = Math.round((float)row - tetrisGame.getDraggedPiece().getHeight() / 2.0f);

                    //Verificacion de si la posicion es valida
                    if (isValidPlacement(tetrisGame.getDraggedPiece(), startX, startY)) {
                        //Se coloca la pieza
                        placePiece(tetrisGame.getDraggedPiece(), startX, startY);

                        //Se verifica si se a completado una fila o columna
                        for (int i = 0; i < DIMENSIO; i++) {
                            if (isRowFilled(i)) {
                                removeRow(i);
                            }
                            if (isColumnFilled(i)) {
                                removeColumn(i);
                            }
                        }

                        tetrisGame.updatePiece();
                        
                    } else {
                        //Posicion invalida
                        //No se hace nada
                    }
                    tetrisGame.setDraggedPiece(null); // Clear the dragged piece
                    repaint();
                }
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateMousePosition(e);
                repaint();
            }
        });
        
        previewPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tetrisGame.setCurrentPieceToDragged(); //Se ccrea una copia de la pieza actual
                updateMousePosition(e);
                repaint();
            }
        });
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D) { //Rotatcion horaria con la tecla "d"
            if (tetrisGame.getCurrentPiece() != null) {
                tetrisGame.getCurrentPiece().rotateClockwise();
                previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                previewPanel.repaint();
                repaint(); //Actualizar el tablero
            }
        } else if (keyCode == KeyEvent.VK_A) { //Rotacion antihorario con la tecla "a"
            if (tetrisGame.getCurrentPiece() != null) {
                tetrisGame.getCurrentPiece().rotateCounterClockwise();
                previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                previewPanel.repaint();
                repaint(); //Actualizar el tablero
            }
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Necesario para el funcionamiento pero esta vacio
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Necesario para el funcionamiento pero esta vacio
    }
    
    //Actualizacionde la posicion del raton en la pantalla
    public void updateMousePosition(MouseEvent e) {
        mousePosition = e.getPoint();
    }
    
    //Metodo que comprueba si es una posicion valida
    private boolean isValidPlacement(TetrisPiece piece, int startX, int startY) {
        Point adjustedStart = new Point(startX, startY);
        piece = truncatePieceIfNeeded(piece, adjustedStart);

        if (piece == null) {
            return false; //Posicion invalida
        }

        boolean[][] shape = piece.getShape();
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();


        //Verificacion de las coliciones con los bordes del tablero
        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j]) {
                    int row = adjustedStart.y + i;
                    int col = adjustedStart.x + j;
                    if (row < 0 || row >= DIMENSIO || col < 0 || col >= DIMENSIO) {
                        //Posicion invalida
                        return false;
                    }
                    if (t[row][col].isOcupada()) {
                        //Posicion invalida
                        return false;
                    }
                }
            }
        }

        return true;
    }   

    //Metodo que coloca la pieza en el tablero
    private void placePiece(TetrisPiece piece, int startX, int startY) {
        Point adjustedStart = new Point(startX, startY);
        piece = truncatePieceIfNeeded(piece, adjustedStart);

        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();
        boolean[][] shape = piece.getShape();

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j]) {
                    int row = adjustedStart.y + i;
                    int col = adjustedStart.x + j;
                    if (row >= 0 && row < DIMENSIO && col >= 0 && col < DIMENSIO) {
                        t[row][col].setOcupada(true);
                        t[row][col].setTexture();
                    }
                }
            }
        }
        piece.resetRotationState();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Se dibuja el tablero
        for (int i = 0; i < DIMENSIO; i++) {
            for (int j = 0; j < DIMENSIO; j++) {
                if (t[i][j].isOcupada()) {
                    //Se dibuja la textura al lugar adecuado
                    Image textureImage = t[i][j].getTexture().getImage();
                    g2d.drawImage(textureImage, (int) t[i][j].getRec().getX(), (int) t[i][j].getRec().getY(), COSTAT, COSTAT, null);
                } else {
                    //Se dibujan las casillas vacias
                    g2d.setColor(Color.WHITE);
                    g2d.fill(t[i][j].getRec());
                }
            }
        }

        // Se dibuja la pieza que sigue el ratón tras haberla seleccionado en el preview panel
        if (tetrisGame.getDraggedPiece() != null && mousePosition != null) {
            boolean[][] shape = tetrisGame.getDraggedPiece().getShape();
            int pieceWidth = tetrisGame.getDraggedPiece().getWidth();
            int pieceHeight = tetrisGame.getDraggedPiece().getHeight();
            // Se ajusta la posición de la pieza al centro del ratón para que sea
            // más intuitivo colocarla en el tablero
            int startX = mousePosition.x - (pieceWidth * COSTAT) / 2;
            int startY = mousePosition.y - (pieceHeight * COSTAT) / 2;

            Image draggedPieceTexture = new ImageIcon(getClass().getResource(Casella.getOcuppiedCellTexture())).getImage();
            for (int i = 0; i < pieceHeight; i++) {
                for (int j = 0; j < pieceWidth; j++) {
                    if (shape[i][j]) {
                        int x = startX + j * COSTAT;
                        int y = startY + i * COSTAT;
                        // Se dibuja la textura en lugar de un color sólido
                        g2d.drawImage(draggedPieceTexture, x, y, COSTAT, COSTAT, null);
                    }
                }
            }
        }
    }
    
    private TetrisPiece truncatePieceIfNeeded(TetrisPiece piece, Point start) {
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        boolean[][] shape = piece.getShape();

        //Truncamiento de la parte derecha en caso necessario
        shape = piece.truncateRight(shape, start.x, pieceWidth, pieceHeight, DIMENSIO);

        //Truncamiento de la parte inferior en caso necessario
        shape = piece.truncateBottom(shape, start.y, pieceWidth, pieceHeight, DIMENSIO);

        //Truncamiento de la parte izquierda en caso necessario
        shape = piece.truncateLeft(shape, start.x, pieceWidth, pieceHeight);

        //Truncamiento de la parte superior en caso necessario
        shape = piece.truncateTop(shape, start.y, pieceWidth, pieceHeight);

        piece.setShape(shape);
        piece.setWidth(shape[0].length);
        piece.setHeight(shape.length);

        return piece;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAXIM, MAXIM);
    }
    
    //Eliminacion de la fila rellena
    private void removeRow(int row) {
        for (int j = 0; j < DIMENSIO; j++) {
            t[row][j].setOcupada(false);
            t[row][j].setTexture(); // Pass the file name directly
            gameMenu.increaseScore(); //Se recompensa el jugador
        }
    }

    //Eliminacion de la columna rellena
    private void removeColumn(int col) {
        for (int i = 0; i < DIMENSIO; i++) {
            t[i][col].setOcupada(false);
            t[i][col].setTexture(); // Pass the file name directly
            gameMenu.increaseScore(); //Se penaliza el jugador
        }
    }
    
    //Comprobacion de una fila rellena
    private boolean isRowFilled(int row) {
        for (int j = 0; j < DIMENSIO; j++) {
            if (!t[row][j].isOcupada()) {
                return false;
            }
        }
        return true;
    }
    
    //Comprobacion de una columna rellena
    private boolean isColumnFilled(int col) {
        for (int i = 0; i < DIMENSIO; i++) {
            if (!t[i][col].isOcupada()) {
                return false;
            }
        }
        return true;
    }
}