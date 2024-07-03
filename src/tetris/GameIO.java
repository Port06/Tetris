package tetris;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//Clase encargada de leer i escribir del fichero serializado para crear objetos
//en la clase game 

public class GameIO {
    
    //Fichero de la serializacion para almacenar partidas
    private static final String GAME_DATA_FILE = "assets/partidasTetrisUIB.dat";
    private static List<Game> completedGames = new ArrayList<>();
    
    private static JFrame frame;
    
    public GameIO() {
    
        frame = Tetris.frame;
        
    }
    
    // Load game history from the file
    public void loadGameHistory() {
        File gameDataFile = new File(GAME_DATA_FILE);
        if (gameDataFile.exists() && gameDataFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GAME_DATA_FILE))) {
                completedGames = (List<Game>) ois.readObject();
            } catch (EOFException e) {
                System.out.println("Reached end of file unexpectedly.");
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading game data. The file might be corrupt or incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException e) {
                System.out.println("Game data file not found, starting fresh.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading game data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Game data file not found or empty, starting fresh.");
        }
    }
    
    // Save game history to the file
    private void saveGameHistory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GAME_DATA_FILE))) {
            oos.writeObject(completedGames);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving game data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void addCompletedGame(Game game) {
        completedGames.add(game);
        saveGameHistory();
    }
    
    public List<Game> getCompletedGames() {
        return completedGames;
    }
    
}
