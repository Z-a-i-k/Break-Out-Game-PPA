import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    // Position stored as ratios relative to the canvas dimensions.
    private double xRatio, yRatio;
    // Radius stored as a ratio relative to the canvas width.
    private double radiusRatio;
    // Velocity components stored as ratios relative to the canvas width.
    private double vxRatio, vyRatio;
    
    private static final double REFERENCE_WIDTH = 800; // Reference used for velocity ratios

    /**
     * Create a Ball.
     */
    public Ball(double x, double y, double radius, double vx, double vy, double canvasWidth, double canvasHeight) {
        this.xRatio = x / canvasWidth;
        this.yRatio = y / canvasHeight;
        this.radiusRatio = radius / canvasWidth;  
        // Store velocity as a ratio relative to a reference width.
        this.vxRatio = vx / REFERENCE_WIDTH;
        this.vyRatio = vy / REFERENCE_WIDTH;
    }
    
    /**
     * Updates the ball's position and handles bouncing off the walls.
     */
    public void update(double canvasWidth, double canvasHeight) {
        double effectiveVx = vxRatio * canvasWidth;
        double effectiveVy = vyRatio * canvasWidth;  // using canvasWidth for uniform scaling
        
        double absX = xRatio * canvasWidth + effectiveVx;
        double absY = yRatio * canvasHeight + effectiveVy;
        double radius = getRadius(canvasWidth);
        
        // Bounce off left and right walls.
        if (absX - radius < 0) {
            absX = radius;
            vxRatio = -vxRatio;
        } else if (absX + radius > canvasWidth) {
            absX = canvasWidth - radius;
            vxRatio = -vxRatio;
        }
        
        // Bounce off the top wall.
        if (absY - radius < 0) {
            absY = radius;
            vyRatio = -vyRatio;
        }
        // Bounce off the bottom wall (handled in Game for life loss).
        if (absY + radius > canvasHeight) {
            absY = canvasHeight - radius;
            vyRatio = -vyRatio;
        }
        
        xRatio = absX / canvasWidth;
        yRatio = absY / canvasHeight;
    }
    
    /**
     * Draws the ball on the canvas.
     */
    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        double absX = xRatio * canvasWidth;
        double absY = yRatio * canvasHeight;
        double radius = getRadius(canvasWidth);
        
        gc.setFill(Color.WHITE);
        gc.fillOval(absX - radius, absY - radius, radius * 2, radius * 2);
    }
    
    /**
     * Returns the effective absolute radius  based on the current canvas width
     */
    public double getRadius(double canvasWidth) {
        return radiusRatio * canvasWidth;
    }
    
    /**
     * Increases the ball's speed by a given percentage.
     */
    public void increaseSpeed(double percentage) {
        vxRatio *= (1 + percentage);
        vyRatio *= (1 + percentage);
    }
    
    /**
     * Returns the ball's absolute y position
     */
    public double getY(double canvasHeight) {
        return yRatio * canvasHeight;
    }
    
    /**
     * Returns the ball's absolute x position
     */
    public double getX(double canvasWidth) {
        return xRatio * canvasWidth;
    }
    
    /**
     * Resets the ball's position and velocity.
     */
    public void reset(double x, double y, double vx, double vy, double canvasWidth, double canvasHeight) {
        this.xRatio = x / canvasWidth;
        this.yRatio = y / canvasHeight;
        this.vxRatio = vx / REFERENCE_WIDTH;
        this.vyRatio = vy / REFERENCE_WIDTH;
    }
    
    /**
     * Checks for collision between the ball (circle) and a rectangle.
     */
    public boolean collidesWith(double rectX, double rectY, double rectWidth, double rectHeight, double canvasWidth, double canvasHeight) {
        double absX = xRatio * canvasWidth;
        double absY = yRatio * canvasHeight;
        double radius = getRadius(canvasWidth);
        double closestX = clamp(absX, rectX, rectX + rectWidth);
        double closestY = clamp(absY, rectY, rectY + rectHeight);
        double dx = absX - closestX;
        double dy = absY - closestY;
        return (dx * dx + dy * dy) < (radius * radius);
    }
    
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Inverts the ball's vertical velocity.
     */
    public void invertVerticalVelocity() {
        vyRatio = -vyRatio;
    }
    
    /**
     * Sets the ball's yRatio to a new value.
     */
    public void setYRatio(double newYRatio) {
        this.yRatio = newYRatio;
    }
    
        public double getEffectiveSpeed(double canvasWidth) {
        double effectiveVx = Math.abs(vxRatio * canvasWidth);
        double effectiveVy = Math.abs(vyRatio * canvasWidth);
        // Compute the magnitude of the velocity vector.
        return Math.sqrt(effectiveVx * effectiveVx + effectiveVy * effectiveVy);
    }
    
    public double getEffectiveVx(double canvasWidth) {
        return Math.abs(vxRatio * canvasWidth);
    }
    
    /**
     * Returns the effective vertical velocity in pixels per update,
     * based on the current canvas width.
     */
    public double getEffectiveVy(double canvasWidth) {
        return Math.abs(vyRatio * canvasWidth);
    }
    
    /**
    * Returns the ball's base horizontal velocity (in pixels per update)
    * based on the REFERENCE_WIDTH.
    */
    public double getBaseVx() {
    return vxRatio * REFERENCE_WIDTH;
    }

    /**
    * Returns the ball's base vertical velocity (in pixels per update)
    * based on the REFERENCE_WIDTH.
    */
    public double getBaseVy() {
    return vyRatio * REFERENCE_WIDTH;
    }
}
