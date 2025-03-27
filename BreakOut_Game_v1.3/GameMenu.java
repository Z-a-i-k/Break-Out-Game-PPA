import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * A simple implementation of the Menu bar at the top of the gaming window. 
 * Resets highest score, quits the game and contains the "About" section :)
 */

public class GameMenu
{
    private MenuBar menuBar;

    public GameMenu()
    {
        final Menu fileMenu = new Menu("File");
        final Menu helpMenu = new Menu("Help");
        
             
        MenuItem quitMenuItem = new MenuItem("Quit");
        MenuItem resetMenuItem = new MenuItem("Reset Highscore");
        MenuItem aboutMenuItem = new MenuItem("About");
        
        fileMenu.getItems().addAll(resetMenuItem, quitMenuItem);
        helpMenu.getItems().add(aboutMenuItem);
        
        resetMenuItem.setOnAction(this::handleReset);
        quitMenuItem.setOnAction(this::handleQuit);
        aboutMenuItem.setOnAction(this::handleAbout);
        
        menuBar = new MenuBar();    
        menuBar.getMenus().addAll(fileMenu, helpMenu);
    }
    
    public MenuBar getMenuBar(){
        return menuBar;
    }
        
    private void handleQuit(ActionEvent event) {
        // Exiting the game with a delay of a min
        PauseTransition pause = new PauseTransition(Duration.millis(60));
        
        pause.setOnFinished(e -> { Platform.exit(); System.exit(0);});
        pause.play();
    }
    
    private void handleAbout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        alert.setTitle("About");
        alert.setHeaderText("PPA Block-Breaker");
        alert.setContentText("Title: PPA Block-Breaker\nAuthors: Lucas Lobo, José Pires, Mark Soltyk, Kais Ali");
        
        alert.showAndWait();
        
    }
    
    private void handleReset(ActionEvent event) {
        ScoreManager.resetHighScore();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset High Score");
        alert.setHeaderText(null);
        alert.setContentText("High score has been reset!");
        alert.showAndWait();
    }
}
