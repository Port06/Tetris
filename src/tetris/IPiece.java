package tetris;

import java.awt.Color;

public class IPiece extends TetrisPiece {
    private static final int[][] SHAPE = {
        {1},
        {1},
        {1},
        {1}
    };

    public IPiece(Color color) {
        super(color);
    }

    @Override
    public int[][] getShape() {
        return SHAPE;
    }

    @Override
    public void rotateClockwise() {
        // Implement rotation logic
    }

    @Override
    public void rotateCounterClockwise() {
        // Implement rotation logic
    }
}
