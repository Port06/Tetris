package tetris;

import java.util.Random;
import java.util.List;

public class TetrisGame {
    
    private List<TetrisPiece> availablePieces;
    private TetrisPiece currentPiece;
    private TetrisPiece draggedPiece;
    
    //Estos son los atributos de las puntuaciones del jugador y del tiempo de partida
    private static String playerName;
    private static int playerScore = 0;
    private static int changeFormScoreCost = -5;
    private static int rotateFormScoreCost = -2;
    private static int removeCellCost = 1;
    private static int gameTime;
    private static int totalGameTime = 100;
    
    // Variables adicionales para acceder a otras clases
    private PreviewPanel previewPanel;
    private Tauler tauler;

    public TetrisGame() {
        // Cargar las piezas disponibles llamando al método createPieces de TetrisPiece
        availablePieces = TetrisPiece.createPieces();
        currentPiece = selectRandomPiece();
    }
    
    //A partir de aquí se encuentran los getters y setters
    
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
    
    public PreviewPanel getPreviewPanel() {
        return previewPanel;
    }

    public Tauler getTauler() {
        return tauler;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public int getPlayerScore() {
        return playerScore;
    }
    
    public int getChangeFormCost() {
        return changeFormScoreCost;
    }
    
    public int getRotateFormScoreCost() {
        return rotateFormScoreCost;
    }
    
    public int getRemoveCellCost() {
        return removeCellCost;
    }
    
    public int getGameTime() {
        return gameTime;
    }
    
    public int getTotalGameTime() {
        return totalGameTime;
    }
    
    public void setDraggedPiece(TetrisPiece draggedPiece) {
        this.draggedPiece = draggedPiece;
    }
    
    public void setCurrentPieceToDragged() {
        this.draggedPiece = currentPiece;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
    
    public void setChangeFormCost(int changeFormScoreCost) {
        this.changeFormScoreCost = changeFormScoreCost;
    }
    
    public void setRotateFormScoreCost(int rotateFormScoreCost) {
        this.rotateFormScoreCost = rotateFormScoreCost;
    }
    
    public void setRemoveCellCost(int removeCellCost) {
        this.removeCellCost = removeCellCost;
    }
    
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }
    
    public void setTotalGameTime(int totalGameTime) {
        this.totalGameTime = totalGameTime;
    }
    
    
    //A partir de aquí hay la lógica addicional del funcionamiento del tetris
    
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
}