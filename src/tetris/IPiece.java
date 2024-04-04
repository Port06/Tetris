package tetris;

import java.awt.Color;

public class IPiece extends TetrisPiece {
    private static final boolean[][] SHAPE = {
        {true, false, false},
        {true, false, false},
        {true, false, false}
    };

    public IPiece() {
        super(SHAPE);
    }
    
    @Override
    public int getWidth() {
        return 1; // Width of the I piece
    }

    @Override
    public int getHeight() {
        return 3; // Height of the I piece
    }

    @Override
    public boolean[][] getShape() {
        return SHAPE;
    }

    public void rotateClockwise() {
        // Implement rotation logic
    }

    public void rotateCounterClockwise() {
        // Implement rotation logic
    }
}
