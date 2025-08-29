package com.zekecode.hakai;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.GameLoop;
import com.zekecode.hakai.engine.GameManager;
import com.zekecode.hakai.engine.LevelLoader;
import com.zekecode.hakai.engine.UIManager;
import com.zekecode.hakai.engine.data.BrickTypeData;
import com.zekecode.hakai.engine.data.LayoutData;
import com.zekecode.hakai.engine.data.LevelData;
import com.zekecode.hakai.engine.input.InputHandler;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.systems.*;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The central orchestrator for the entire game. This class creates, initializes, and manages all
 * major game components.
 */
public class Game {

  private final GraphicsContext gc;
  private final Scene scene;

  private GameLoop gameLoop;

  public Game(GraphicsContext gc, Scene scene) {
    this.gc = gc;
    this.scene = scene;
  }

  /**
   * Initializes all game systems, loads the level, and wires everything together. This is the
   * "Composition Root" of the game engine.
   */
  public void initialize() {
    // --- 1. CREATE CORE INFRASTRUCTURE ---
    EventBus eventBus = new EventBus();
    World world = new World();
    EntityFactory entityFactory = new EntityFactory(world);
    InputManager inputManager = new InputManager();

    // --- 2. CREATE GAME LOGIC & MANAGER ---
    GameManager gameManager = new GameManager(world);

    // --- 3. CREATE SYSTEMS AND REGISTER LISTENERS ---
    BrickSystem brickSystem = new BrickSystem(world);

    eventBus.register(brickSystem);

    world.addSystem(new RenderSystem(gc));
    world.addSystem(new MovementSystem(inputManager));
    world.addSystem(new CollisionSystem(world, eventBus));
    world.addSystem(new PhysicsSystem(800, 600, eventBus)); // Use constants or pass them in
    world.addSystem(brickSystem);

    // --- 4. CREATE UI & INPUT HANDLERS ---
    UIManager uiManager = new UIManager(800, 600);
    InputHandler inputHandler = new InputHandler(inputManager, gameManager);
    inputHandler.attach(scene);

    // --- 5. LOAD LEVEL AND SPAWN ENTITIES ---
    loadLevel("level_1.yml", entityFactory);
    entityFactory.createPlayer(800 / 2.0 - 50, 600 - 50);
    entityFactory.createBall(800 / 2.0 - 7.5, 600 / 2.0);

    // --- 6. CREATE THE GAME LOOP ---
    this.gameLoop = new GameLoop(gameManager, uiManager, gc);
  }

  /** Starts the main game loop. */
  public void run() {
    if (gameLoop == null) {
      throw new IllegalStateException("Game has not been initialized. Call initialize() first.");
    }
    gameLoop.start();
  }

  /** Stops the main game loop. */
  public void stop() {
    if (gameLoop != null) {
      gameLoop.stop();
    }
  }

  private void loadLevel(String levelFile, EntityFactory entityFactory) {
    LevelLoader levelLoader = new LevelLoader();
    LevelData level = levelLoader.loadLevel(levelFile);
    LayoutData layout = level.layout;

    List<String> pattern = layout.pattern;
    for (int row = 0; row < pattern.size(); row++) {
      String rowPattern = pattern.get(row);
      for (int col = 0; col < rowPattern.length(); col++) {
        char brickChar = rowPattern.charAt(col);
        if (brickChar == '.') continue;
        BrickTypeData type = level.brickTypes.get(brickChar);
        if (type == null) continue;

        double brickWidth = (type.width != null) ? type.width : layout.defaultBrickWidth;
        double brickHeight = (type.height != null) ? type.height : layout.defaultBrickHeight;
        double x = (col * (layout.defaultBrickWidth + layout.padding)) + layout.padding;
        double y = (row * (layout.defaultBrickHeight + layout.padding)) + layout.offsetTop;

        entityFactory.createBrick(x, y, brickWidth, brickHeight, Color.web(type.color), type.hp);
      }
    }
  }
}
