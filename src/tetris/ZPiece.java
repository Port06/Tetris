package tetris;

import java.awt.Color;

public class ZPiece extends TetrisPiece {
    private static final boolean[][] SHAPE = {
        {true, true, false},
        {false, true, true},
        {false, false, false}
    };

    public ZPiece() {
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
