import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {
    // Store the player's position and size as ratios (0.0 - 1.0) of the canvas dimensions.
    private double xRatio;       // e.g., 0.4 means 40% of the canvas width from the left.
    private double yRatio;
    private double widthRatio;
    private double heightRatio;
    
    // Speed stored as a ratio relative to the canvas width.
    private double speedRatio;  
    
    private int life;
    private boolean noLivesLeft;

    /**
     * Construct a Player using absolute values along with the initial canvas dimensions.
     * The ratios are computed from these initial values.
     */
    public Player(double x, double y, double width, double height, double speed, double canvasWidth, double canvasHeight) {
        this.xRatio = x / canvasWidth;
        this.yRatio = y / canvasHeight;
        this.widthRatio = width / canvasWidth;
        this.heightRatio = height / canvasHeight;
        this.speedRatio = speed / canvasWidth;
        this.life = 3;
        this.noLivesLeft = false;
    }
    
        public void reduceLife(){
        if (life > 0) {
            life--;
            if (life <= 0) {
                life = 0;
                noLivesLeft = true;
            }
        }
    }

    
    public int getLife() {
        return life;
    }
    
    /**
     * Move the player left by decreasing the absolute x-position,
     * then update the xRatio accordingly.
     */
    public void moveLeft(double canvasWidth) {
        double effectiveSpeed = speedRatio * canvasWidth * 6;
        double absX = xRatio * canvasWidth;
        absX -= effectiveSpeed;  // speed is in pixels
        xRatio = absX / canvasWidth;
    }

    /**
     * Move the player right by increasing the absolute x-position,
     * then update the xRatio accordingly.
     */
    public void moveRight(double canvasWidth) {
        double effectiveSpeed = speedRatio * canvasWidth * 6;
        double absX = xRatio * canvasWidth;
        absX += effectiveSpeed;
        xRatio = absX / canvasWidth;
    }
    
    /**
     * Wrap the player around the canvas horizontally.
     */
    public void wrap(double canvasWidth) {
        double absX = xRatio * canvasWidth;
        double absWidth = widthRatio * canvasWidth;
        if (absX + absWidth < 0) {    // off the left side
            absX = canvasWidth;
        } else if (absX > canvasWidth) {  // off the right side
            absX = -absWidth;
        }
        xRatio = absX / canvasWidth;
    }
    
    /**
     * Draw the player using the current canvas dimensions.
     */
    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        double absX = xRatio * canvasWidth;
        double absY = yRatio * canvasHeight;
        double absWidth = widthRatio * canvasWidth;
        double absHeight = heightRatio * canvasHeight;
        
        gc.setFill(Color.WHITE);
        gc.fillRect(absX, absY, absWidth, absHeight);
    }
    
    public void resetLives() {
        this.life = 3;
        this.noLivesLeft = false;
    }
    
        public double getAbsX(double canvasWidth) {
        return xRatio * canvasWidth;
    }
    
    public double getAbsY(double canvasHeight) {
        return yRatio * canvasHeight;
    }
    
    public double getAbsWidth(double canvasWidth) {
        return widthRatio * canvasWidth;
    }
  

    public double getAbsHeight(double canvasHeight) {
        return heightRatio * canvasHeight;
    }
    
    public void clampPosition(double canvasWidth) {
        double absX = getAbsX(canvasWidth);
        double absWidth = getAbsWidth(canvasWidth);
        if (absX < 0) {
            absX = 0;
        } else if (absX + absWidth > canvasWidth) {
            absX = canvasWidth - absWidth;
        }
        // Update xRatio based on the new absolute position.
        xRatio = absX / canvasWidth;
    }


    

}
