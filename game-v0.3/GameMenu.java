import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.application.Platform;

/**
 * Write a description of class MenuBar here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameMenu
{
    // instance variables - replace the example below with your own
    private MenuBar menuBar;

    /**
     * Constructor for objects of class MenuBar
     */
    public GameMenu()
    {
        final Menu fileMenu = new Menu("File");
        final Menu helpMenu = new Menu("Help");
        
             
        MenuItem quitMenuItem = new MenuItem("Quit");
        MenuItem aboutMenuItem = new MenuItem("About");
        
        
        fileMenu.getItems().add(quitMenuItem);
        helpMenu.getItems().add(aboutMenuItem);
        
        quitMenuItem.setOnAction(this::handleQuit);
        aboutMenuItem.setOnAction(this::handleAbout);
        
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);
    }
    
    public MenuBar getMenuBar(){
        return menuBar;
    }
    
    private void handleQuit(ActionEvent event) {
        Platform.exit();
    }
    
    private void handleAbout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        alert.setTitle("About");
        alert.setHeaderText("PPA Breakout");
        alert.setContentText("Title: PPA BreakOut\nAuthors: Lucas, ");
        
        alert.showAndWait();
        
    }
}
