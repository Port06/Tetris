
package tetris;

//Este es el main del programa y es el encargado de generar la ventana donde 
//habra el tetris funcional

public class Main {
    
    private static Tetris tetris; 
    
    public static void main(String[] args) {
        tetris = new Tetris(); 
        SettingsLogic settingsLogic = new SettingsLogic(tetris.frame, tetris.getButtonsAndIcons(), tetris.getTetrisGame(), tetris.getGameMenu());
        
        settingsLogic.setTetris(tetris); // Setting the Tetris instance in SettingsLogic
    }
}