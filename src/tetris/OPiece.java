package tetris;

import java.awt.Color;

public class OPiece extends TetrisPiece {
    private static final boolean[][] SHAPE = {
        {true, true, false},
        {true, true, false},
        {false, false, false}
    };

    public OPiece() {
        super(SHAPE);
    }
    
    @Override
    public int getWidth() {
        return 2; // Width of the I piece
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
