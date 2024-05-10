package tetris;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class TetrisGame {
    
    private List<TetrisPiece> availablePieces;

    public TetrisGame() {
        // Cargar las piezas disponibles llamando al método createPieces de TetrisPiece
        availablePieces = TetrisPiece.createPieces();
    }
    
    //Metodo para escoger una pieza al azar
    public TetrisPiece selectRandomPiece() {
        Random random = new Random();
        int randomIndex = random.nextInt(availablePieces.size());
        return availablePieces.get(randomIndex);
    }
}
