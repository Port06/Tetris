
package tetris;

//Este es el main del programa y es el encargado de generar la ventana donde 
//habra el tetris funcional

public class Main {
    
    private static Tetris tetris; 
    
    public static void main(String[] args) {
        
        //Aqui se crean todas las intancias necessarias para el programa
        GameIO gameIO = new GameIO();
        GameMenu gameMenu = new GameMenu();
        
        tetris = new Tetris(gameIO); 
        
        SettingsLogic settingsLogic = new SettingsLogic(tetris.frame, tetris.getButtonsAndIcons(), tetris.getTetrisGame(), tetris.getGameMenu());
       
        
        settingsLogic.setTetris(tetris);
        gameMenu.setTetris(tetris, gameIO);
    }
}