import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ReinforcedBlock1 extends Block {
    private final int maxHitCounter;
    
    public ReinforcedBlock1(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.hitCounter = 2;
        this.maxHitCounter = 2;
        this.points = 150;
        this.color = Color.GREEN.desaturate(); // Initial color.
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            Color displayColor;
            if (hitCounter == maxHitCounter) {
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
