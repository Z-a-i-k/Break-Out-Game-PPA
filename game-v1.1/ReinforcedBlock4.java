import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ReinforcedBlock4 extends Block {
    private final int maxHitCounter;
    
    public ReinforcedBlock4(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.hitCounter = 5;
        this.maxHitCounter = 5;
        this.points = 300;
        this.color = Color.RED.desaturate(); // Initial color.
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            Color displayColor;
            if (hitCounter == maxHitCounter) {
                displayColor = Color.RED.desaturate();
            } else if (hitCounter == maxHitCounter - 1) {
                displayColor = Color.ORANGE.desaturate();
            } else if (hitCounter == maxHitCounter - 2) {
                displayColor = Color.YELLOW.desaturate();
            } else if (hitCounter == maxHitCounter - 3) {
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
