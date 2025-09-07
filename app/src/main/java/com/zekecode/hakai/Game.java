package com.zekecode.hakai;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.*;
import com.zekecode.hakai.engine.data.LevelData;
import com.zekecode.hakai.engine.fx.BackgroundManager;
import com.zekecode.hakai.engine.fx.sounds.SoundManager;
import com.zekecode.hakai.engine.input.InputHandler;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.engine.ui.UIManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.systems.*;
import com.zekecode.hakai.systems.collisions.BallBrickCollisionSystem;
import com.zekecode.hakai.systems.collisions.BallPaddleCollisionSystem;
import com.zekecode.hakai.systems.collisions.CollisionSystem;
import com.zekecode.hakai.systems.collisions.PaddlePowerUpCollisionSystem;
import com.zekecode.hakai.systems.powerups.PowerUpEffectSystem;
import com.zekecode.hakai.systems.powerups.PowerUpSpawnSystem;
import com.zekecode.hakai.systems.rendering.EntityRenderer;
import com.zekecode.hakai.systems.rendering.RenderSystem;
import com.zekecode.hakai.systems.rendering.RendererFactory;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * The central orchestrator for the entire game. This class creates, initializes, and manages all
 * major game components.
 */
public class Game {

  private final GraphicsContext gc;
  private final Scene scene;

  private GameLoop gameLoop;
  private BackgroundManager backgroundManager;

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
    UIManager uiManager = new UIManager(800, 600);
    SoundManager soundManager = new SoundManager();
    List<EntityRenderer> renderers = RendererFactory.createRenderers();

    // --- 2. CREATE GAME LOGIC & MANAGER ---
    GameManager gameManager = new GameManager(world);
    LevelManager levelManager = new LevelManager(entityFactory);

    // --- 3. CREATE SYSTEMS AND REGISTER LISTENERS ---
    BrickSystem brickSystem = new BrickSystem(eventBus);
    ScoreSystem scoreSystem = new ScoreSystem(world, eventBus);
    PlayerStateSystem playerStateSystem = new PlayerStateSystem(world, eventBus);
    BallSystem ballSystem = new BallSystem(inputManager, entityFactory);
    BallPaddleCollisionSystem ballPaddleCollisionSystem = new BallPaddleCollisionSystem(eventBus);
    BallBrickCollisionSystem ballBrickCollisionSystem = new BallBrickCollisionSystem(eventBus);
    PowerUpSpawnSystem powerUpSpawnSystem = new PowerUpSpawnSystem(entityFactory);
    PaddlePowerUpCollisionSystem paddlePowerUpCollisionSystem =
        new PaddlePowerUpCollisionSystem(eventBus);
    PowerUpEffectSystem powerUpEffectSystem = new PowerUpEffectSystem();

    eventBus.register(gameManager);
    uiManager.registerEventHandlers(eventBus);
    eventBus.register(soundManager);
    eventBus.register(brickSystem);
    eventBus.register(scoreSystem);
    eventBus.register(playerStateSystem);
    eventBus.register(ballSystem);
    eventBus.register(ballPaddleCollisionSystem);
    eventBus.register((ballBrickCollisionSystem));
    eventBus.register(powerUpSpawnSystem);
    eventBus.register(paddlePowerUpCollisionSystem);
    eventBus.register(powerUpEffectSystem);

    world.addSystem(new RenderSystem(gc, renderers));
    world.addSystem(new MovementSystem(inputManager));
    world.addSystem(new CollisionSystem(eventBus));
    world.addSystem(new PhysicsSystem(800, 600, eventBus));
    world.addSystem(ballSystem);
    world.addSystem(brickSystem);
    world.addSystem(scoreSystem);
    world.addSystem(playerStateSystem);
    world.addSystem(powerUpEffectSystem);

    // --- 4. INPUT HANDLERS ---
    InputHandler inputHandler = new InputHandler(inputManager, gameManager);
    inputHandler.attach(scene);

    // --- 5. LOAD LEVEL, INITIALIZE BACKGROUND, AND SPAWN ENTITIES ---
    LevelData level = levelManager.loadAndBuildLevel("level_2.yml");
    this.backgroundManager = new BackgroundManager(level.background, 800, 600);
    entityFactory.createPlayerState();
    entityFactory.createPlayer(800 / 2.0 - 50, 600 - 50);
    entityFactory.createBall(800 / 2.0 - 7.5, 600 / 2.0);

    // --- 6. CREATE THE GAME LOOP ---
    this.gameLoop = new GameLoop(gameManager, uiManager, backgroundManager, gc);
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
}
