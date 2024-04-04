package tetris;

import java.awt.Color;


// Aquí se encuentra la clase abstracta que define lo que es una pieza de tetris
public abstract class TetrisPiece {
    private boolean[][] shape;

    public TetrisPiece(boolean[][] shape) {
        this.shape = shape;
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
}
