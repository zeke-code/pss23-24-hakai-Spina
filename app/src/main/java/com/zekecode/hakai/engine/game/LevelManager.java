package com.zekecode.hakai.engine.game;

import com.zekecode.hakai.config.data.BrickTypeData;
import com.zekecode.hakai.config.data.LayoutData;
import com.zekecode.hakai.config.data.LevelData;
import com.zekecode.hakai.entities.EntityFactory;
import java.util.List;
import javafx.scene.paint.Color;

/** Orchestrates loading level data and using it to populate the world with entities. */
public class LevelManager {

  private final EntityFactory entityFactory;
  private final LevelLoader levelLoader;

  public LevelManager(EntityFactory entityFactory) {
    this.entityFactory = entityFactory;
    this.levelLoader = new LevelLoader();
  }

  /**
   * Loads level data from a file and constructs the corresponding brick entities in the world.
   *
   * @param levelFile The name of the level file (e.g., "level_1.yml").
   * @return The fully loaded LevelData object, which can be used for other setups (like
   *     backgrounds).
   */
  public LevelData loadAndBuildLevel(String levelFile) {
    LevelData level = levelLoader.loadLevel(levelFile);
    LayoutData layout = level.layout;

    List<String> pattern = layout.pattern;
    for (int row = 0; row < pattern.size(); row++) {
      String rowPattern = pattern.get(row);
      for (int col = 0; col < rowPattern.length(); col++) {
        char brickChar = rowPattern.charAt(col);
        if (brickChar == '.') {
          continue; // Skip empty spaces
        }

        BrickTypeData type = level.brickTypes.get(brickChar);
        if (type == null) {
          System.err.println("Warning: Brick type '" + brickChar + "' not defined in level file.");
          continue;
        }

        // Determine brick dimensions, using defaults from the layout if not specified on the type
        double brickWidth = (type.width != null) ? type.width : layout.defaultBrickWidth;
        double brickHeight = (type.height != null) ? type.height : layout.defaultBrickHeight;

        // Calculate brick position based on grid, padding, and offset
        double x = (col * (layout.defaultBrickWidth + layout.padding)) + layout.padding;
        double y = (row * (layout.defaultBrickHeight + layout.padding)) + layout.offsetTop;

        entityFactory.createBrick(
            x, y, brickWidth, brickHeight, Color.web(type.color), type.hp, type.powerUp);
      }
    }
    return level;
  }
}
