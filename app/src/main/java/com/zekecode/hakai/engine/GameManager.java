package com.zekecode.hakai.engine;

import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.data.BrickTypeData;
import com.zekecode.hakai.engine.data.LayoutData;
import com.zekecode.hakai.engine.data.LevelData;
import com.zekecode.hakai.engine.enums.GameState;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.systems.CollisionSystem;
import com.zekecode.hakai.systems.MovementSystem;
import com.zekecode.hakai.systems.PhysicsSystem;
import com.zekecode.hakai.systems.RenderSystem;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** Manages the high-level game state (e.g., Running, Paused) and the ECS World. */
public class GameManager {

  private final World world;
  private final EntityFactory entityFactory;
  private final LevelLoader levelLoader;
  private GameState currentState;

  public GameManager() {
    this.world = new World();
    this.entityFactory = new EntityFactory(world);
    this.levelLoader = new LevelLoader();
    this.currentState = GameState.RUNNING;
  }

  public void setup(
      GraphicsContext gc, InputManager inputManager, double screenWidth, double screenHeight) {
    setupSystems(gc, inputManager, screenWidth, screenHeight);
    setupEntities(screenWidth, screenHeight);
  }

  private void setupSystems(
      GraphicsContext gc, InputManager inputManager, double screenWidth, double screenHeight) {
    world.addSystem(new RenderSystem(gc));
    world.addSystem(new MovementSystem(inputManager));
    world.addSystem(new PhysicsSystem(screenWidth, screenHeight));
    world.addSystem(new CollisionSystem(world));
  }

  private void setupEntities(double screenWidth, double screenHeight) {
    // Create entities not defined in the level file (player, ball)
    entityFactory.createPlayer(screenWidth / 2.0 - 50, screenHeight - 50);
    entityFactory.createBall(screenWidth / 2.0 - 7.5, screenHeight / 2.0);

    // --- LOAD LEVEL AND SPAWN BRICKS ---
    // TODO: remove hardcoded level filename and move setup logic elsewhere
    LevelData level = levelLoader.loadLevel("level_1.yml");
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
          continue; // Skip undefined brick types
        }

        // Handle variable brick sizes: use the specific size if present, else use the default
        double brickWidth = (type.width != null) ? type.width : layout.defaultBrickWidth;
        double brickHeight = (type.height != null) ? type.height : layout.defaultBrickHeight;

        // Calculate position based on the default grid spacing
        double x = (col * (layout.defaultBrickWidth + layout.padding)) + layout.padding;
        double y = (row * (layout.defaultBrickHeight + layout.padding)) + layout.offsetTop;

        entityFactory.createBrick(
            x,
            y,
            brickWidth,
            brickHeight,
            Color.web(type.color), // The .web() method parses hex color strings
            type.hp);
      }
    }
  }

  // The main update method is now only concerned with the game logic
  public void update(double deltaTime) {
    if (currentState == GameState.RUNNING) {
      world.update(deltaTime);
    }
  }

  public void togglePause() {
    currentState = (currentState == GameState.RUNNING) ? GameState.PAUSED : GameState.RUNNING;
  }

  public GameState getCurrentState() {
    return currentState;
  }
}
