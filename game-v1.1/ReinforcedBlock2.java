import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ReinforcedBlock2 extends Block {
    private final int maxHitCounter;
    
    public ReinforcedBlock2(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.hitCounter = 3;
        this.maxHitCounter = 3;
        this.points = 200;
        this.color = Color.YELLOW.desaturate(); // Initial color.
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            Color displayColor;
            if (hitCounter == maxHitCounter) {
                displayColor = Color.YELLOW.desaturate();
            } else if (hitCounter == maxHitCounter - 1) { // after one hit
                displayColor = Color.GREEN.desaturate();
            } else { // hitCounter == 1
                displayColor = Color.LIGHTBLUE.desaturate();
            }
            gc.setFill(displayColor);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }
}
