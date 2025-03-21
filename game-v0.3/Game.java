import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToolBar;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.control.Tab;



/**
 * Write a description of class Game here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Game extends Application
{
    // instance variables - replace the example below with your own
    private Player player;
    
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private Canvas canvas;
    private BlockManager blocks;
    static Color canvas_color = Color.rgb(51,51,51);
    
    
     // Constants for canvas dimensions
    /**
     * Constructor for objects of class Game
     */
    
    @Override
    public void start(Stage stage)
    {
       
        BorderPane pane = new BorderPane();
        GameMenu gameMenu = new GameMenu();
        StackPane gamePane = new StackPane();

        
        
        pane.setTop(gameMenu.getMenuBar());
        pane.setCenter(gamePane);
        
        canvas = new Canvas();
        
        canvas.widthProperty().bind(gamePane.widthProperty());
        canvas.heightProperty().bind(gamePane.heightProperty());
        gamePane.getChildren().add(canvas);
        
        
        gamePane.setMinSize(0, 0);
        pane.setMinSize(0, 0);
        
    
       


        
        Scene scene = new Scene(pane, 800,600);
        
        stage.setScene(scene);
        stage.setTitle("Breakout Game");
        
        stage.setMinWidth(300);
        stage.setMinHeight(300);
        stage.show();
       
         blocks = new BlockManager();
        blocks.generateBlocks(canvas.getWidth(), canvas.getHeight(), 50, 6, 30, 5);
        
        player = new Player(350, 500, 100, 10, 2, 800, 600);
        
        
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    leftPressed = true;
                    System.out.println("left key is pressed");
                    break;
                case RIGHT:
                    rightPressed = true;
                    System.out.println("right key is pressed");
                    break;
                default:
                    break;
            }
        });
        
         scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    leftPressed = false;
                    System.out.println("left key is released");
                    break;
                case RIGHT:
                    rightPressed = false;
                    System.out.println("right key is released");
                    break;
                default:
                    break;
            }
        });
        
        // Obtain the GraphicsContext for the canvas.
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Create and start the game loop using AnimationTimer.
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();   // Update game state (e.g., positions, collisions, etc.)
                render(gc); // Draw the current state on the canvas.
            }
        };
        
        
        gameLoop.start();
       
    }
    
        // Update game logic here.
    private void update() {
        
        double currentCanvasWidth = canvas.getWidth();
        
        if (leftPressed) {
            player.moveLeft(currentCanvasWidth);
        }
        
        if (rightPressed) {
            player.moveRight(currentCanvasWidth);
        }
        
        player.wrap(currentCanvasWidth);
    }

    // Render game objects using vector graphics on the provided GraphicsContext.
    private void render(GraphicsContext gc) {
       double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        
        // Clear the canvas with a background color.
        gc.setFill(canvas_color);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Calculate margin and effective width
        double margin = canvasWidth * 0.05;
        double effectiveWidth = canvasWidth - 2 * margin;
        
         // Update each block's absolute values from its relative values.
        for (Block block : blocks.getBlocks()) {
        block.x = margin + block.xRatio * effectiveWidth;
        block.width = block.widthRatio * effectiveWidth;
        block.y = block.yRatio * canvasHeight;
        block.height = block.heightRatio * canvasHeight;
        }
        
        blocks.drawBlocks(gc);
        
        // Draw the player using the current canvas dimensions.
        player.draw(gc, canvasWidth, canvasHeight);
        
    
        
    }

}
