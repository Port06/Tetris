package tetris;

import java.awt.Color;

public class LPiece extends TetrisPiece {
    private static final boolean[][] SHAPE = {
        {false, false, true},
        {true, true, true},
        {false, false, false}
    };

    public LPiece() {
        super(SHAPE);
    }
    
    @Override
    public int getWidth() {
        return 3; // Width of the I piece
    }

    @Override
    public int getHeight() {
        return 2; // Height of the I piece
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
