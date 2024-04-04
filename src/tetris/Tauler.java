package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Tauler extends JPanel {

    public static final int DIMENSIO = 20;
    private static final int MAXIM = 500;
    private static final int COSTAT = MAXIM / DIMENSIO;
    private Casella[][] t;
    private PreviewPanel previewPanel;
    private TetrisPiece currentPiece;
    private TetrisPiece nextPiece;

    public Tauler(PreviewPanel previewPanel) {
        
        this.previewPanel = previewPanel;
        currentPiece = selectRandomPiece();
        nextPiece = selectRandomPiece();
        
        
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

                    // Se llama al metodo de seleccionde pieza aleatoria
                    TetrisPiece selectedPiece = updateNextPiece();

                    // Calculamos la posicion para colocar dicha pieza
                    int startX = col - selectedPiece.getWidth() / 2;
                    int startY = row - selectedPiece.getHeight() / 2;

                    // Verificamos si el posicionamiento es valido
                    if (isValidPlacement(selectedPiece, startX, startY)) {
                        // Colocamos la pieza
                        placePiece(selectedPiece, startX, startY);
                        repaint();
                    }
                }
            }
        });
    }

    //Metodo para escoger una pieza al azar
    public TetrisPiece selectRandomPiece() {
        TetrisPiece[] pieces = {new IPiece(), new JPiece(), new LPiece(), new OPiece(), new SPiece(), new TPiece(), new ZPiece()};
        Random random = new Random();
        int randomIndex = random.nextInt(pieces.length);

        return pieces[randomIndex];
    }
    
    private TetrisPiece updateNextPiece() {
        
        currentPiece = nextPiece;
        nextPiece = selectRandomPiece();
        previewPanel.setPreviewPiece(nextPiece); // Update the preview panel with the next piece
        previewPanel.repaint();
        
        return currentPiece;
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

}