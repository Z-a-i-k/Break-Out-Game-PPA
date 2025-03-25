// BlockManager.java
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

public class BlockManager {
    private List<Block> blocks;

    public BlockManager() {
        blocks = new ArrayList<>();
    }


    // Updated generateBlocks now accepts a level parameter (1 to 5)
    // In BlockManager.java
    public void generateBlocks(double canvasWidth, double canvasHeight, double startY, int numRows, double rowHeight, int level) {
    blocks.clear();
    // Define margin (e.g., 5% on each side)
    
    double margin = canvasWidth * 0.05;
    double effectiveWidth = canvasWidth - 2 * margin;

    // Set a range for block width that scales proportionally.
    // The original values (50 and 80) are based on a reference width of 800.
    double minBlockWidth = effectiveWidth * 50 / 800;  // 50 scaled relative to effective width
    double maxBlockWidth = effectiveWidth * 80 / 800;  // 80 scaled relative to effective width
    
    double currentY = startY;
    for (int row = 0; row < numRows; row++) {
        double currentX = margin;  // Start at the left margin
        while (currentX < margin + effectiveWidth) {
            double remainingWidth = (margin + effectiveWidth) - currentX;
            // If remaining width is too small for a new block, add it to the last block.
            if (remainingWidth < minBlockWidth) {
                if (!blocks.isEmpty()) {
                    Block lastBlock = blocks.get(blocks.size() - 1);
                    // Increase last block's width to fill the row.
                    lastBlock.width += remainingWidth;
                    // Also update its relative width (will be recalculated in render)
                    lastBlock.widthRatio = lastBlock.width / effectiveWidth;
                }
                break; // Exit the loop for this row.
            }
            
            // Generate a random block width in the desired range.
            double blockWidth = minBlockWidth + Math.random() * (maxBlockWidth - minBlockWidth);
            // Clamp blockWidth so it doesn't exceed the remaining width.
            if (blockWidth > remainingWidth) {
                blockWidth = remainingWidth;
            }
            
            Block block = null;
            double randomValue = Math.random();
            // Choose block type based on level (example logic)
            if (level == 1) {
                block = new StandardBlock(currentX, currentY, blockWidth, rowHeight);
            } else if (level == 2) {
                if (randomValue < 0.5) {
                    block = new StandardBlock(currentX, currentY, blockWidth, rowHeight);
                } else {
                    block = new ReinforcedBlock1(currentX, currentY, blockWidth, rowHeight);
                }
            } else if (level == 3) {
                if (randomValue < 0.33) {
                    block = new StandardBlock(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.66) {
                    block = new ReinforcedBlock1(currentX, currentY, blockWidth, rowHeight);
                } else {
                    block = new ReinforcedBlock2(currentX, currentY, blockWidth, rowHeight);
                }
            } else if (level == 4) {
                if (randomValue < 0.25) {
                    block = new StandardBlock(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.5) {
                    block = new ReinforcedBlock1(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.75) {
                    block = new ReinforcedBlock2(currentX, currentY, blockWidth, rowHeight);
                } else {
                    block = new ReinforcedBlock3(currentX, currentY, blockWidth, rowHeight);
                }
            } else if (level >= 5) {  // For level 5 and above, include all block types.
                if (randomValue < 0.2) {
                    block = new StandardBlock(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.4) {
                    block = new ReinforcedBlock1(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.6) {
                    block = new ReinforcedBlock2(currentX, currentY, blockWidth, rowHeight);
                } else if (randomValue < 0.8) {
                    block = new ReinforcedBlock3(currentX, currentY, blockWidth, rowHeight);
                } else {
                    block = new ReinforcedBlock4(currentX, currentY, blockWidth, rowHeight);
                }
            }
            
            // Set relative positions/sizes based on the effective area.
            block.xRatio = (currentX - margin) / effectiveWidth;
            block.widthRatio = blockWidth / effectiveWidth;
            block.yRatio = currentY / canvasHeight;
            block.heightRatio = rowHeight / canvasHeight;
            
            blocks.add(block);
            currentX += blockWidth; // Advance to the next position.
        }
        currentY += rowHeight; // Next row.
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
