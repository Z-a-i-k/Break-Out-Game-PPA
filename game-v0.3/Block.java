import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Block {
    protected double x, y;         // Position
    protected double width, height; // Dimensions
    protected int hitCounter;      // Hits remaining before breaking
    protected int points;          // Points awarded per hit (or when broken)
    protected Color color;          // Block color
    
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

    // Call this method when the block is hit.
    // It decrements the hit counter and returns the points awarded for that hit.
    public int hit() {
        if (!isDestroyed()) {
            hitCounter--;
            return points;
        }
        return 0;
    }

    // Returns true if the block is broken.
    public boolean isDestroyed() {
        return hitCounter <= 0;
    }

    // Draw the block. Each subclass can override this to provide custom visual effects.
    public abstract void draw(GraphicsContext gc);
}
