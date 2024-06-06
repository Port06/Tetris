package tetris;

import java.io.Serializable;
import java.time.LocalDateTime;


//Clase partida que se instancia al acabar una partida para realizar la serializacion
//de dicha instancia y almacenarlo asi en el historial
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private LocalDateTime dateTime;
    private int gameTime;
    private int points;

    public Game(String playerName, LocalDateTime dateTime, int gameTime, int points) {
        this.playerName = playerName;
        this.dateTime = dateTime;
        this.gameTime = gameTime;
        this.points = points;
    }

    // Getters
    public String getPlayerName() {
        return playerName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getPoints() {
        return points;
    }

    // Setters
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Game{" +
                "playerName='" + playerName + '\'' +
                ", dateTime=" + dateTime +
                ", gameTime=" + gameTime +
                ", points=" + points +
                '}';
    }
}