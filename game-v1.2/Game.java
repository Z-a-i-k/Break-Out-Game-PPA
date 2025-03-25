import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

public class Game extends Application {
    private Player player;
    private Ball ball;
    private BlockManager blocks;
    
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private Canvas canvas;
    private static Color canvas_color = Color.rgb(51,51,51);
    
    // Game state flags
    private boolean start = false;   // Game updates only when true.
    private boolean gameOver = false;
    private int level = 1;
    
    // Score tracking fields.
    private int points = 0;
    private int highscore = 0;
    
    // Initial ball parameters (absolute values for an 800x600 reference)
    private final double initialBallX = 400;
    private final double initialBallY = 495;
    private final double initialBallRadius = 4;
    private final double initialBallVX = 2;
    private final double initialBallVY = 2;
    
    // Initial player parameters (absolute values for an 800x600 reference)
    private final double initialPlayerX = 350;
    private final double initialPlayerY = 500;
    private final double initialPlayerWidth = 100;
    private final double initialPlayerHeight = 10;
    private final double initialPlayerSpeed = 2;
    
    // Ball speed increase mechanism.
    private int blocksBroken = 0;
    private final int SPEED_INCREASE_THRESHOLD = 3;  // Increase speed after this many blocks are broken.
    private final double SPEED_INCREASE_PERCENTAGE = 0.2; // Increase speed by 20%.
    private final double MAX_EFFECTIVE_SPEED = 6.0; // Maximum effective speed.
    
    // Block generation parameters.
    private final double blockStartY = 50;
    private final int blockRowsBase = 6;
    private final double blockRowHeight = 30;
    
    @Override
    public void start(Stage stage) {
        // Load highscore from file.
        highscore = loadHighscore();
        points = 0;
        
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
        
        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Breakout Game");
        stage.setMinWidth(300);
        stage.setMinHeight(300);
        stage.show();
       
        
        
        // Initialize BlockManager and generate blocks for level 1.
        blocks = new BlockManager();
        Platform.runLater(() -> {
            double adjustedRowHeight = canvas.getHeight() * blockRowHeight / 600;
            blocks.generateBlocks(canvas.getWidth(), canvas.getHeight(), blockStartY, blockRowsBase, adjustedRowHeight, level);
        });
        
        // Initialize player and ball (using our 800x600 reference).
        player = new Player(initialPlayerX, initialPlayerY, initialPlayerWidth, initialPlayerHeight, initialPlayerSpeed, 800, 600);
        ball = new Ball(initialBallX, initialBallY, initialBallRadius, initialBallVX, initialBallVY, 800, 600);
        
        // Combined key event handler.
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    if (!gameOver) {
                        start = true;
                    }
                    break;
                case ENTER:
                    if (gameOver) {
                        // Reset game on game over.
                        player = new Player(initialPlayerX, initialPlayerY, initialPlayerWidth, initialPlayerHeight, initialPlayerSpeed, 800, 600);
                        player.resetLives(); // Resets lives to 3.
                        points = 0;
                        level = 1;
                        // Calculate adjusted row height based on current canvas height.
                        double adjustedRowHeight = canvas.getHeight() * blockRowHeight / 600;
                        blocks.generateBlocks(canvas.getWidth(), canvas.getHeight(), blockStartY, blockRowsBase, adjustedRowHeight, level);
                        double cW = canvas.getWidth();
                        double cH = canvas.getHeight();
                        double pX = player.getAbsX(cW);
                        double pY = player.getAbsY(cH);
                        double pW = player.getAbsWidth(cW);
                        double ballRadius = ball.getRadius(cW);
                        double ballX = pX + pW / 2;
                        double ballY = pY - ballRadius - 1;
                        // Reset ball with initial speed.
                        ball.reset(ballX, ballY, initialBallVX, initialBallVY, cW, cH);
                        gameOver = false;
                        start = true;
                        blocksBroken = 0;
                    }
                    break;
                case ESCAPE:
                    if (!gameOver) {
                        start = !start;  // Toggle pause/resume.
                    }
                    break;
                case LEFT:
                    leftPressed = true;
                    break;
                case RIGHT:
                    rightPressed = true;
                    break;
                default:
                    break;
            }
        });
        
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    leftPressed = false;
                    break;
                case RIGHT:
                    rightPressed = false;
                    break;
                default:
                    break;
            }
        });
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Game loop.
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        gameLoop.start();
    }
    
    // Update game logic.
    private void update() {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        
        if (start) {
            ball.update(canvasWidth, canvasHeight);
            
            if (leftPressed) {
                player.moveLeft(canvasWidth);
            }
            if (rightPressed) {
                player.moveRight(canvasWidth);
            }
            // Instead of wrapping, we now clamp the player's position.
            player.clampPosition(canvasWidth);
            
            // --- Collision with player (paddle) ---
            double pX = player.getAbsX(canvasWidth);
            double pY = player.getAbsY(canvasHeight);
            double pW = player.getAbsWidth(canvasWidth);
            double pH = player.getAbsHeight(canvasHeight);
            if (ball.collidesWith(pX, pY, pW, pH, canvasWidth, canvasHeight)) {
                ball.invertVerticalVelocity();
                double ballRadius = ball.getRadius(canvasWidth);
                double newY = pY - ballRadius - 1;
                ball.setYRatio(newY / canvasHeight);
            }
            
            // --- Collision with blocks ---
            Iterator<Block> it = blocks.getBlocks().iterator();
            while (it.hasNext()) {
                Block block = it.next();
                if (ball.collidesWith(block.x, block.y, block.width, block.height, canvasWidth, canvasHeight)) {
                    ball.invertVerticalVelocity();
                    block.hit();
                    if (block.isDestroyed()) {
                        points += block.points; // Award points when block is fully destroyed.
                        it.remove();
                        blocksBroken++;
                        if (blocksBroken >= SPEED_INCREASE_THRESHOLD) {
                            if (ball.getEffectiveSpeed(canvasWidth) < MAX_EFFECTIVE_SPEED) {
                                ball.increaseSpeed(SPEED_INCREASE_PERCENTAGE);
                            }
                            blocksBroken = 0;
                        }
                    }
                    break;  // Process one collision per update.
                }
            }
            
            // --- Check if ball hits bottom ---
            double ballBottom = ball.getY(canvasHeight) + ball.getRadius(canvasWidth);
            if (ballBottom >= canvasHeight) {
            player.reduceLife();
                if (player.getLife() <= 0) {
                    start = false;
                    gameOver = true;
                    if (points > highscore) {
                        highscore = points;
                        saveHighscore(highscore);
                    }
                } else {
                    double pAbsX = player.getAbsX(canvasWidth);
                    double pAbsY = player.getAbsY(canvasHeight);
                    double pAbsW = player.getAbsWidth(canvasWidth);
                    double ballRadius = ball.getRadius(canvasWidth);
                    double ballX = pAbsX + pAbsW / 2;
                    double ballY = pAbsY - ballRadius - 1;
                    // Preserve the ball's current base speed (not scaled by canvas width)
                    double currentVx = ball.getBaseVx();
                    double currentVy = ball.getBaseVy();
                    ball.reset(ballX, ballY, currentVx, currentVy, canvasWidth, canvasHeight);
                    start = false;
                
            

                }
            }
            
            // --- Level change: when all blocks are cleared ---
            if (blocks.getBlocks().isEmpty()) {
                level++;
                // Calculate adjusted row height based on current canvas height.
                double adjustedRowHeight = canvas.getHeight() * blockRowHeight / 600;
                blocks.generateBlocks(canvas.getWidth(), canvas.getHeight(), blockStartY, blockRowsBase, adjustedRowHeight, level);
    
                double pAbsX = player.getAbsX(canvas.getWidth());
                double pAbsY = player.getAbsY(canvas.getHeight());
                double pAbsW = player.getAbsWidth(canvas.getWidth());
                double ballRadius = ball.getRadius(canvas.getWidth());
                double ballX = pAbsX + pAbsW / 2;
                double ballY = pAbsY - ballRadius - 1;
                // Preserve ball's current speed: using current effective velocities (or base velocities if you've updated that logic).
                double currentVx = ball.getBaseVx();
                double currentVy = ball.getBaseVy();
                ball.reset(ballX, ballY, currentVx, currentVy, canvas.getWidth(), canvas.getHeight());
                start = false;
                blocksBroken = 0;
            }
        }
    }
    
    // Render game objects.
    private void render(GraphicsContext gc) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        highscore = ScoreManager.getHighScore();

        
        // Clear canvas.
        gc.setFill(canvas_color);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Update each block's absolute values.
        double margin = canvasWidth * 0.05;
        double effectiveWidth = canvasWidth - 2 * margin;
        for (Block block : blocks.getBlocks()) {
            block.x = margin + block.xRatio * effectiveWidth;
            block.width = block.widthRatio * effectiveWidth;
            block.y = block.yRatio * canvasHeight;
            block.height = block.heightRatio * canvasHeight;
        }
        
        blocks.drawBlocks(gc);
        ball.draw(gc, canvasWidth, canvasHeight);
        player.draw(gc, canvasWidth, canvasHeight);
        
        // Display messages.
        gc.setFill(Color.WHITE);
        if (!start && !gameOver) {
            if (points == 0) {
                gc.fillText("Press SPACE to start", canvasWidth / 2 - 70, canvasHeight / 2);
            } else {
                gc.fillText("Press SPACE to continue", canvasWidth / 2 - 70, canvasHeight / 2);
            }
        }
        if (gameOver) {
            gc.fillText("GAME OVER - Press ENTER to start again", canvasWidth / 2 - 120, canvasHeight / 2);
        }
        
        gc.fillText("Lives: " + player.getLife(), 10, canvasHeight - 10);
        gc.fillText("Points: " + points, 10, 20);
        gc.fillText("Highscore: " + highscore, canvasWidth - 120, 20);

        gc.fillText("Level: " + level, canvasWidth - 80, canvasHeight - 10);
    }
    
    // Load highscore from file.
    private int loadHighscore() {
        try {
            if (Files.exists(Paths.get("highscore.txt"))) {
                String content = new String(Files.readAllBytes(Paths.get("highscore.txt")));
                
                ScoreManager.setHighScore(Integer.parseInt(content.trim()));
                
                return Integer.parseInt(content.trim());
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    // Save highscore to file.
    private void saveHighscore(int score) {
        try {
            Files.write(Paths.get("highscore.txt"), String.valueOf(score).getBytes(), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                
            ScoreManager.setHighScore(score);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
