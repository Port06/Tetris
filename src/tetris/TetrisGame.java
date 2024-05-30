package tetris;

import java.util.Random;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TetrisGame {
    
    private List<TetrisPiece> availablePieces;
    private TetrisPiece currentPiece;
    private TetrisPiece draggedPiece;
    
    // Variables adicionales para acceder a otras clases
    private PreviewPanel previewPanel;
    private Tauler tauler;

    public TetrisGame() {
        // Cargar las piezas disponibles llamando al método createPieces de TetrisPiece
        availablePieces = TetrisPiece.createPieces();
        currentPiece = selectRandomPiece();
    }
    
    public void setPreviewPanel(PreviewPanel previewPanel) {
        this.previewPanel = previewPanel;
    }
    
    public void setTauler(Tauler tauler) {
        this.tauler = tauler;
    }
    
    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }
    
    public TetrisPiece getDraggedPiece() {
        return draggedPiece;
    }
    
    public void setDraggedPiece(TetrisPiece draggedPiece) {
        this.draggedPiece = draggedPiece;
    }
    
    public void setCurrentPieceToDragged() {
        this.draggedPiece = currentPiece;
    }
    
    // Metodo para escoger una pieza al azar
    public TetrisPiece selectRandomPiece() {
        Random random = new Random();
        int randomIndex = random.nextInt(availablePieces.size());
        return availablePieces.get(randomIndex);
    }
    
    public void updatePiece() {
        // Update for a new piece
        currentPiece = selectRandomPiece();
        // Update the preview panel with the next piece
        previewPanel.setPreviewPiece(currentPiece);
        previewPanel.repaint();
        tauler.repaint();
    }

    public PreviewPanel getPreviewPanel() {
        return previewPanel;
    }

    public Tauler getTauler() {
        return tauler;
    }
}