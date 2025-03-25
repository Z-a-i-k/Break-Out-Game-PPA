import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StandardBlock extends Block {

    
    public StandardBlock(double x, double y, double width, double height) {
            super(x, y, width, height);
            
            this.hitCounter = 1;
            this.points = 100;
            this.color = Color.LIGHTBLUE.desaturate();
        }

    @Override
    public void draw(GraphicsContext gc) {
        if (!isDestroyed()) {
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }
    

}
