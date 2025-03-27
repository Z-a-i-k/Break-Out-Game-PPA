import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Initial Implementation of a Block
 * An abstract block from where every other block will inhereit basic attributes 
 */
public abstract class Block {
    protected double x, y;        
    protected double width, height; 
    protected int hitCounter;      
    protected int points;  
    
    protected Color color;          
    
    public double xRatio;
    public double yRatio;
    public double widthRatio;
    public double heightRatio;

    
    public Block(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
    }

    // Destoys the Block - only applies to the Breaking blocks at the top of the game
    public int hit() {
        if (!isDestroyed()) {
            hitCounter--;
            return points;
        }
        return 0;
    }

    public boolean isDestroyed() {
        return hitCounter <= 0;
    }

    // To be override later :)
    public abstract void draw(GraphicsContext gc);
}
