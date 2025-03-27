import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Block Manager is a class that creates and manages the rows of blocks, their max width, number of rows - their width height.
 * Responsible for creting different difficulty levels in terms of blocks and what modifying blocks reinforcement making them harder to break
 */

public class BlockManager {
    
    private List<Block> blocks;

    
    public BlockManager() {
        blocks = new ArrayList<>();
    }

    public void generateBlocks(double canvasWidth, double canvasHeight, double startY, int numRows, double rowHeight, int level) {
        
        blocks.clear();
        double margin = canvasWidth * 0.01;
        double effectiveWidth = canvasWidth - 1 * margin;
        double minBlockWidth = effectiveWidth * 50 / 800;
        double maxBlockWidth = effectiveWidth * 80 / 800;
        double currentY = startY;
        
        for (int row = 0; row < numRows; row++) {
            
            double currentX = margin;
            
            while (currentX < margin + effectiveWidth) {
                
                double remainingWidth = margin + effectiveWidth - currentX;
                
                if (remainingWidth < minBlockWidth * 0.5) {
                    if (!blocks.isEmpty()) {
                        Block lastBlock = blocks.get(blocks.size() - 1);
                        lastBlock.width += remainingWidth;
                        lastBlock.widthRatio = lastBlock.width / effectiveWidth;
                    }
                    break;
                }

                double blockWidth = minBlockWidth + Math.random() * (maxBlockWidth - minBlockWidth);
                
                if (blockWidth > remainingWidth) {
                    blockWidth = remainingWidth;
                }
                
                Block block = createBlock(currentX, currentY, blockWidth, rowHeight, level);
                block.xRatio = (currentX - margin) / effectiveWidth;
                block.widthRatio = blockWidth / effectiveWidth;
                block.yRatio = currentY / canvasHeight;
                block.heightRatio = rowHeight / canvasHeight;
                blocks.add(block);
                currentX += blockWidth;
                
            }
            
            currentY += rowHeight;
        }
    }

    private Block createBlock(double x, double y, double width, double height, int level) {
        
        double r = Math.random();
        
        if (level == 1) {

            return new StandardBreakingBlock(x, y, width, height);
           
        } 
        else if (level == 2) {
            
            if (r < 0.5) return new StandardBreakingBlock(x, y, width, height);
            return new ReinforcedBreakingBlock(x, y, width, height, 1); 
                            
        } 
        else if (level == 3) {
            
            if (r < 0.33) return new StandardBreakingBlock(x, y, width, height);
            if (r < 0.66) return new ReinforcedBreakingBlock(x, y, width, height, 1);
            return new ReinforcedBreakingBlock(x, y, width, height, 2);
              
        } 
        else if (level == 4) {
            
            if (r < 0.25) return new StandardBreakingBlock(x, y, width, height);
            if (r < 0.5) return new ReinforcedBreakingBlock(x, y, width, height, 1);
            if (r < 0.75) return new ReinforcedBreakingBlock(x, y, width, height, 2);
            return new ReinforcedBreakingBlock(x, y, width, height, 3);

        } 
        else {
            
            if (r < 0.2) return new StandardBreakingBlock(x, y, width, height);
            if (r < 0.4) return new ReinforcedBreakingBlock(x, y, width, height, 1);
            if (r < 0.6) return new ReinforcedBreakingBlock(x, y, width, height, 2);
            if (r < 0.8) return new ReinforcedBreakingBlock(x, y, width, height, 3);
            return new ReinforcedBreakingBlock(x, y, width, height, 4);
            
        }
    }

    public void drawBlocks(GraphicsContext gc) {
        
        for (Block block : blocks) {
            block.draw(gc);
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
