import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Simple implmentation of a breaking block, contains code for advancing difficulty of blocks
 * Every tier block become harder to break, but they score more points.
 * Block's colour suggests it's diffuculty
 */

public class ReinforcedBreakingBlock extends Block {
    
    private final int maxHitCounter;
    private final Color[] colors;

    public ReinforcedBreakingBlock(double x, double y, double width, double height, int tier) {
        
        super(x, y, width, height);
        
        this.maxHitCounter = tier + 1;
        this.hitCounter = tier + 1;
        this.points = 100 + 50 * tier;
        this.colors = getColorsForTier(tier);
    }


    private Color[] getColorsForTier(int tier) {
        if (tier == 1) {
            return new Color[]{Color.GREEN.desaturate(), Color.LIGHTBLUE.desaturate()};
        } else if (tier == 2) {
            return new Color[]{Color.YELLOW.desaturate(), Color.GREEN.desaturate(), Color.LIGHTBLUE.desaturate()};
        } else if (tier == 3) {
            return new Color[]{Color.ORANGE.desaturate(), Color.YELLOW.desaturate(), Color.GREEN.desaturate(), Color.LIGHTBLUE.desaturate()};
        } else if (tier == 4) {
            return new Color[]{Color.RED.desaturate(), Color.ORANGE.desaturate(), Color.YELLOW.desaturate(), Color.GREEN.desaturate(), Color.LIGHTBLUE.desaturate()};
        }
        
        else  throw new RuntimeException("Smh wrong with tier! It is - " + tier );
    }

    @Override
    public void draw(GraphicsContext gc) {
        
        if (!isDestroyed()) {
            int colorIndex = maxHitCounter - hitCounter;
            gc.setFill(colors[colorIndex]);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }
}