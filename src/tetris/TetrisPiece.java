package tetris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TetrisPiece {

    public String name;
    private boolean[][] shape;
    private int rotationState; // Estado de rotación actual

    public TetrisPiece(String name, boolean[][] shape) {
        this.name = name;
        this.shape = shape;
        this.rotationState = 0; // Inicialmente sin rotación
    }

    private static final String PIECES_FILE = "C:/Users/Usuari/OneDrive/Documentos/NetBeansProjects/Tetris/assets/pieces.txt";

    public static List<TetrisPiece> createPieces() {
        List<TetrisPiece> pieces = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PIECES_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                TetrisPiece piece = parsePiece(line);
                if (piece != null) {
                    pieces.add(piece);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pieces;
    }

    private static TetrisPiece parsePiece(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        boolean[][] shape = new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                shape[i][j] = Boolean.parseBoolean(parts[i * 3 + j + 1]);
            }
        }
        return new TetrisPiece(name, shape);
    }
    
    // Método para copiar una matriz de forma
    private boolean[][] copyShape(boolean[][] shape) {
        boolean[][] copy = new boolean[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, copy[i], 0, shape[i].length);
        }
        return copy;
    }

    public boolean[][] getShape() {
        return shape;
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getHeight() {
        return shape.length;
    }
    
    public void resetRotationState() {
        rotateShape(4 - rotationState); // Aplica la rotación inversa para restablecerla a su orientación original
        rotationState = 0; // Reinicia el estado de rotación a 0
    }

    // Métodos de rotación ajustados para tener en cuenta el estado de rotación
    public void rotateClockwise() {
        rotationState = (rotationState + 1) % 4; // Incrementa el estado de rotación (módulo 4)
        rotateShape(1); // Aplica la rotación de la forma
    }

    public void rotateCounterClockwise() {
        rotationState = (rotationState + 3) % 4; // Decrementa el estado de rotación (módulo 4)
        rotateShape(3); // Aplica la rotación de la forma en sentido contrario
    }

    private void rotateShape(int rotations) {
        for (int r = 0; r < rotations; r++) {
            boolean[][] rotatedShape = new boolean[shape[0].length][shape.length];
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    rotatedShape[j][shape.length - 1 - i] = shape[i][j];
                }
            }
            shape = rotatedShape;
        }
    }
}
