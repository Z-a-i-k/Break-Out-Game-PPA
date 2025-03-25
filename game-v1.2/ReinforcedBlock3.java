import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ReinforcedBlock3 extends Block {
    private final int maxHitCounter;
    
    public ReinforcedBlock3(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.hitCounter = 4;
        this.maxHitCounter = 4;
        this.points = 250;
        this.color = Color.ORANGE.desaturate(); // Initial color.
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            Color displayColor;
            if (hitCounter == maxHitCounter) {
                displayColor = Color.ORANGE.desaturate();
            } else if (hitCounter == maxHitCounter - 1) {
                displayColor = Color.YELLOW.desaturate();
            } else if (hitCounter == maxHitCounter - 2) {
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
