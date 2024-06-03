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

    public Tauler(PreviewPanel previewPanel, TetrisGame tetrisGame, Tetris tetris) {
        
        this.previewPanel = previewPanel;
        this.tetrisGame = tetrisGame;
        this.tetris = tetris;
        
        setFocusable(true); // Allow panel to get focus for keyboard events
        addKeyListener(this); // Register the KeyListener
        setFocusTraversalKeysEnabled(false); // Disable focus traversal keys
        
        // Ensure focus is requested when the component is shown
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

                    // Snap to the nearest grid position
                    int startX = Math.round((float)col - tetrisGame.getDraggedPiece().getWidth() / 2.0f);
                    int startY = Math.round((float)row - tetrisGame.getDraggedPiece().getHeight() / 2.0f);

                    // Check if placement is valid
                    if (isValidPlacement(tetrisGame.getDraggedPiece(), startX, startY)) {
                        // Place the piece
                        placePiece(tetrisGame.getDraggedPiece(), startX, startY);

                        // Check for full rows or columns
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
                        System.out.println("Invalid placement");
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
                tetrisGame.setCurrentPieceToDragged(); // Create a copy of the current piece
                updateMousePosition(e);
                repaint();
            }
        });
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D) { // Rotate clockwise when 'd' is pressed
            if (tetrisGame.getCurrentPiece() != null) {
                tetrisGame.getCurrentPiece().rotateClockwise();
                previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                previewPanel.repaint();
                repaint(); // Repaint the game board to reflect the changes
            }
        } else if (keyCode == KeyEvent.VK_A) { // Rotate counterclockwise when 'a' is pressed
            if (tetrisGame.getCurrentPiece() != null) {
                tetrisGame.getCurrentPiece().rotateCounterClockwise();
                previewPanel.setPreviewPiece(tetrisGame.getCurrentPiece());
                previewPanel.repaint();
                repaint(); // Repaint the game board to reflect the changes
            }
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Empty method
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Empty method
    }
    
    // Method to update the mouse position
    public void updateMousePosition(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    private boolean isValidPlacement(TetrisPiece piece, int startX, int startY) {
        Point adjustedStart = new Point(startX, startY);
        piece = truncatePieceIfNeeded(piece, adjustedStart);

        if (piece == null) {
            System.out.println("Valid placement false");
            return false; // Placement is invalid
        }

        boolean[][] shape = piece.getShape();
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        System.out.println(pieceWidth + " " + pieceHeight);

        // Check for collisions with other pieces and bounds
        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j]) {
                    int row = adjustedStart.y + i;
                    int col = adjustedStart.x + j;
                    if (row < 0 || row >= DIMENSIO || col < 0 || col >= DIMENSIO) {
                        System.out.println("Valid placement false");
                        return false;
                    }
                    if (t[row][col].isOcupada()) {
                        System.out.println("Valid placement false");
                        return false;
                    }
                }
            }
        }

        return true;
    }   

    // Method to place the Tetris pieces on the board
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
                        t[row][col].setTexture("CHOCOLATE.jpg");
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

        // Draw the Tetris board
        for (int i = 0; i < DIMENSIO; i++) {
            for (int j = 0; j < DIMENSIO; j++) {
                if (t[i][j].isOcupada()) {
                    // Draw the texture image at the appropriate location
                    Image textureImage = t[i][j].getTexture().getImage();
                    g2d.drawImage(textureImage, (int) t[i][j].getRec().getX(), (int) t[i][j].getRec().getY(), COSTAT, COSTAT, null);
                } else {
                    // Draw empty squares
                    g2d.setColor(Color.WHITE);
                    g2d.fill(t[i][j].getRec());
                }
            }
        }

        // Draw the dragged piece following the mouse
        if (tetrisGame.getDraggedPiece() != null && mousePosition != null) {
            boolean[][] shape = tetrisGame.getDraggedPiece().getShape();
            int pieceWidth = tetrisGame.getDraggedPiece().getWidth();
            int pieceHeight = tetrisGame.getDraggedPiece().getHeight();
            int startX = mousePosition.x - (pieceWidth * COSTAT) / 2;
            int startY = mousePosition.y - (pieceHeight * COSTAT) / 2;

            for (int i = 0; i < pieceHeight; i++) {
                for (int j = 0; j < pieceWidth; j++) {
                    if (shape[i][j]) {
                        int x = startX + j * COSTAT;
                        int y = startY + i * COSTAT;
                        g2d.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red for the dragging piece
                        g2d.fillRect(x, y, COSTAT, COSTAT);
                    }
                }
            }
        }
    }
    
    private TetrisPiece truncatePieceIfNeeded(TetrisPiece piece, Point start) {
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        boolean[][] shape = piece.getShape();

        // Print original shape for debugging
        System.out.println("Original shape:");
        printShape(shape);

        // Truncate right side if needed and if the columns to be truncated are empty
        shape = piece.truncateRight(shape, start.x, pieceWidth, pieceHeight, DIMENSIO);

        // Truncate bottom side if needed and if the rows to be truncated are empty
        shape = piece.truncateBottom(shape, start.y, pieceWidth, pieceHeight, DIMENSIO);

        // Truncate left side if needed and if the columns to be truncated are empty
        shape = piece.truncateLeft(shape, start.x, pieceWidth, pieceHeight);

        // Truncate top side if needed and if the rows to be truncated are empty
        shape = piece.truncateTop(shape, start.y, pieceWidth, pieceHeight);

        // Print truncated shape for debugging
        System.out.println("Truncated shape:");
        printShape(shape);

        piece.setShape(shape);
        piece.setWidth(shape[0].length);
        piece.setHeight(shape.length);

        return piece;
    }

    private void printShape(boolean[][] shape) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                System.out.print(shape[i][j] ? "X" : " ");
            }
            System.out.println();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAXIM, MAXIM);
    }
    
    // Remove a full row
    private void removeRow(int row) {
        for (int j = 0; j < DIMENSIO; j++) {
            t[row][j].setOcupada(false);
            t[row][j].setTexture("/LIBRE.jpg"); // Pass the file name directly
            tetris.increaseScore(); //Se recompensa el jugador
        }
    }

    // Remove a full column
    private void removeColumn(int col) {
        for (int i = 0; i < DIMENSIO; i++) {
            t[i][col].setOcupada(false);
            t[i][col].setTexture("/LIBRE.jpg"); // Pass the file name directly
            tetris.increaseScore(); //Se penaliza el jugador
        }
    }
    
    private boolean isRowFilled(int row) {
        for (int j = 0; j < DIMENSIO; j++) {
            if (!t[row][j].isOcupada()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isColumnFilled(int col) {
        for (int i = 0; i < DIMENSIO; i++) {
            if (!t[i][col].isOcupada()) {
                return false;
            }
        }
        return true;
    }
}