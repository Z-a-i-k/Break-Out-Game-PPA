import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player {
    private double xRatio;
    private double yRatio;
    private double widthRatio;
    private double heightRatio;
    private double speedRatio;
    
    private int life;
    
    private boolean noLivesLeft;

    
    public Player(double x, double y, double width, double height, double speed, double canvasWidth, double canvasHeight) {
        xRatio = x / canvasWidth;
        yRatio = y / canvasHeight;
        widthRatio = width / canvasWidth;
        heightRatio = height / canvasHeight;
        speedRatio = speed / canvasWidth;
        life = 3;
        noLivesLeft = false;
    }

    public void reduceLife() {
        if (life > 0) {
            life--;
            noLivesLeft = (life == 0);
        }
    }

    public void clampPosition(double canvasWidth) {
        double absX = getAbsX(canvasWidth);
        double absWidth = getAbsWidth(canvasWidth);
        if (absX < 0) {
            absX = 0;
        } else if (absX + absWidth > canvasWidth) {
            absX = canvasWidth - absWidth;
        }
        xRatio = absX / canvasWidth;
    } 


    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) 
    { 
        double absX = xRatio * canvasWidth;
        double absY = yRatio * canvasHeight;
        double absWidth = widthRatio * canvasWidth;
        double absHeight = heightRatio * canvasHeight;
        gc.setFill(Color.WHITE);
        gc.fillRect(absX, absY, absWidth, absHeight);
    }

    public void resetLives() {
        life = 3;
        noLivesLeft = false;
    }

    // --- Moving ---
    public void moveLeft(double canvasWidth) {
        xRatio -= speedRatio * 6;
    }

    public void moveRight(double canvasWidth) {
        xRatio += speedRatio * 6;
    }
    
    // --- Getters ---
        public int getLife() {
        return life;
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

}
