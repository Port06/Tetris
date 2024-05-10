package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.awt.event.*;

public class Tauler extends JPanel implements KeyListener {

    public static final int DIMENSIO = 20;
    private static final int MAXIM = 500;
    private static final int COSTAT = MAXIM / DIMENSIO;
    private Casella[][] t;
    private PreviewPanel previewPanel;
    private TetrisPiece currentPiece;
    
    private TetrisGame tetrisGame;

    public Tauler(PreviewPanel previewPanel, TetrisGame tetrisGame) {
        
        this.tetrisGame = tetrisGame;
        this.previewPanel = previewPanel;
         
        currentPiece = tetrisGame.selectRandomPiece();
        previewPanel.setPreviewPiece(currentPiece);
        
        setFocusable(true); // Allow panel to get focus for keyboard events
        addKeyListener(this); // Register the KeyListener
        
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / COSTAT;
                int col = e.getX() / COSTAT;
                if (row >= 0 && row < DIMENSIO && col >= 0 && col < DIMENSIO) {
                    // Calculate placement position
                    int startX = col - currentPiece.getWidth() / 2;
                    int startY = row - currentPiece.getHeight() / 2;

                    // Check if placement is valid
                    if (isValidPlacement(currentPiece, startX, startY)) {
                        // Place the piece
                        placePiece(currentPiece, startX, startY);
                        
                        // Check for full rows or columns
                        for (int i = 0; i < DIMENSIO; i++) {
                            if (isRowFilled(i)) {
                                removeRow(i);
                            }
                            if (isColumnFilled(i)) {
                                removeColumn(i);
                            }
                        }
                        
                        // Update for a new piece
                        currentPiece = tetrisGame.selectRandomPiece();
                        // Update the preview panel with the next piece
                        previewPanel.setPreviewPiece(currentPiece);
                        previewPanel.repaint();
                        repaint();
                    }
                }
            }
        });
    }
    
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_D) { // Rotate clockwise when 'd' is pressed
                    if (currentPiece != null) {
                        currentPiece.rotateClockwise();
                        previewPanel.setPreviewPiece(currentPiece);
                        previewPanel.repaint();
                        repaint(); // Repaint the game board to reflect the changes
                    }
            } else if (keyCode == KeyEvent.VK_A) { // Rotate counterclockwise when 'a' is pressed
                if (currentPiece != null) {
                    currentPiece.rotateCounterClockwise();
                    previewPanel.setPreviewPiece(currentPiece);
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

    // Metodo que verifica si se puede colocar una pieza de tetris en la casilla presionada
    private boolean isValidPlacement(TetrisPiece piece, int startX, int startY) {
        boolean[][] shape = piece.getShape();
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        // Verificamos si la pieza no sobresale del tablero
        if (startX < 0 || startY < 0 || startX + pieceWidth > DIMENSIO || startY + pieceHeight > DIMENSIO) {
            return false;
        }

        // Verificamos si la pieza no colisiona con otra
        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j]) {
                    int row = startY + i;
                    int col = startX + j;
                    if (row >= 0 && row < DIMENSIO && col >= 0 && col < DIMENSIO && t[row][col].isOcupada()) {
                        return false;
                    }
                }
            }
        }

        // Si no se atasca en los metodos previos es que si se puede colocar
        return true;
    }

    //Metodo para colocar las pieces de tetris en el tablero
    private void placePiece(TetrisPiece piece, int startX, int startY) {
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();
        boolean[][] shape = piece.getShape();

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j]) {
                    int row = startY + i;
                    int col = startX + j;
                    if (row >= 0 && row < DIMENSIO && col >= 0 && col < DIMENSIO) {
                        t[row][col].setOcupada(true);
                        // Cambiar la textura a casilla ocupada
                        t[row][col].setTexture("CHOCOLATE.jpg");
                        //Reiniciar la rotación del tipo de pieza
                        piece.resetRotationState();
                        
                    }
                }
            }
        }
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
            }
        // Clear the top row
        for (int j = 0; j < DIMENSIO; j++) {
            t[0][j].setEmpty(true);
        }
    }

    // Remove a full column
    private void removeColumn(int col) {

            for (int i = 0; i < DIMENSIO; i++) {
                t[i][col].setOcupada(false);
                t[i][col].setTexture("/LIBRE.jpg"); // Pass the file name directly
            }
        // Clear the rightmost column
        for (int i = 0; i < DIMENSIO; i++) {
            t[i][DIMENSIO - 1].setEmpty(true);
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