package tetris;

import java.util.Random;
import java.util.List;


//Esta classe define la mayoria de las acciones que ocurren en una partida
//Estas incluyen las puntución, el nombre del jugador, la generación de piezas
//y la actualizacion de piezas.
public class TetrisGame {
    
    private static GameMenu gameMenu;
    
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
    private static int playerNameMaxLength = 25;  //Aquí se define la longitud
                                                    //máxima del nombre del jugador
    
    // Variables adicionales para acceder a otras clases
    private static PreviewPanel previewPanel;
    private static Tauler tauler;

    public TetrisGame(GameMenu gameMenu) {
        //Inicializar gameMenu
        this.gameMenu = gameMenu;
        
        // Cargar las piezas disponibles llamando al método createPieces de TetrisPiece
        availablePieces = TetrisPiece.createPieces();
        currentPiece = selectRandomPiece();
    }
    
    //A partir de aquí se encuentran los getters y setters
    
    //Los getters
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
    
    public int getPlayerNameMaxLength() {
        return playerNameMaxLength;
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
    
    //Los setters
    public void setDraggedPiece(TetrisPiece draggedPiece) {
        this.draggedPiece = draggedPiece;
    }
    
    public void setPreviewPanel(PreviewPanel previewPanel) {
        this.previewPanel = previewPanel;
    }
    
    public void setTauler(Tauler tauler) {
        this.tauler = tauler;
    }
    
    public void setCurrentPieceToDragged() {
        this.draggedPiece = currentPiece;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setPlayerNameMaxLength(int playerNameMaxLength) {
        this.playerNameMaxLength = playerNameMaxLength;
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
    
    //Metodo de la actualizacion de piezas
    public void updatePiece() {
        // Update for a new piece
        currentPiece = selectRandomPiece();
        // Update the preview panel with the next piece
        previewPanel.setPreviewPiece(currentPiece);
        previewPanel.repaint();
        tauler.repaint();
    }
    
    // Method to increase the score
    public void increaseScore() {
        setPlayerScore(playerScore + removeCellCost);
        gameMenu.updatePlayerScore();
    }
    
}