import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class ReinforcedBlock4 extends Block {

    
public ReinforcedBlock4(double x, double y, double width, double height) {
        super(x, y, width, height);
        
        this.hitCounter = 5;
        this.points = 300;
        this.color = Color.RED.desaturate();
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            // Draw differently to indicate reinforcement (e.g., a different border or gradient).
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }
}