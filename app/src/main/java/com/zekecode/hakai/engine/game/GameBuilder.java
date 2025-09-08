package com.zekecode.hakai;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.config.data.LevelData;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.game.Game;
import com.zekecode.hakai.engine.game.GameLoop;
import com.zekecode.hakai.engine.game.GameManager;
import com.zekecode.hakai.engine.game.LevelManager;
import com.zekecode.hakai.engine.input.InputHandler;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.engine.sounds.SoundManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.entities.EntityRenderer;
import com.zekecode.hakai.entities.RendererFactory;
import com.zekecode.hakai.powerups.EffectRegistry;
import com.zekecode.hakai.systems.*;
import com.zekecode.hakai.systems.RenderSystem;
import com.zekecode.hakai.systems.collisions.BallBrickCollisionSystem;
import com.zekecode.hakai.systems.collisions.BallPaddleCollisionSystem;
import com.zekecode.hakai.systems.collisions.CollisionSystem;
import com.zekecode.hakai.systems.collisions.PaddlePowerUpCollisionSystem;
import com.zekecode.hakai.systems.powerups.EffectManagementSystem;
import com.zekecode.hakai.systems.powerups.PowerUpSystem;
import com.zekecode.hakai.ui.BackgroundManager;
import com.zekecode.hakai.ui.SceneManager;
import com.zekecode.hakai.ui.UIManager;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * A builder class responsible for the entire setup of the game. It creates all managers, systems,
 * and entities, and wires them together, effectively acting as the Composition Root of the
 * application.
 */
public class GameBuilder {

  public Game build(GraphicsContext gc, Scene scene, String levelFile, SceneManager sceneManager) {
    // --- 1. CREATE CORE INFRASTRUCTURE ---
    EventBus eventBus = new EventBus();
    World world = new World();
    EntityFactory entityFactory = new EntityFactory(world);
    InputManager inputManager = new InputManager();
    UIManager uiManager = new UIManager(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    SoundManager soundManager = new SoundManager();
    List<EntityRenderer> renderers = RendererFactory.createRenderers();
    EffectRegistry effectRegistry = new EffectRegistry(entityFactory);
    GameManager gameManager = new GameManager(world, sceneManager);
    LevelManager levelManager = new LevelManager(entityFactory);

    // --- 2. CREATE AND CONFIGURE ALL GAME SYSTEMS & LISTENERS ---
    createAndRegisterSystems(
        eventBus,
        world,
        gc,
        inputManager,
        entityFactory,
        effectRegistry,
        gameManager,
        uiManager,
        soundManager,
        renderers);

    // --- 3. SETUP INPUT HANDLING ---
    new InputHandler(inputManager, gameManager).attach(scene);

    // --- 4. LOAD LEVEL AND CREATE INITIAL ENTITIES ---
    LevelData level = levelManager.loadAndBuildLevel(levelFile);
    BackgroundManager backgroundManager =
        new BackgroundManager(level.background, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

    // Calculate initial positions using GameConfig for clarity and maintainability
    double playerX = (GameConfig.SCREEN_WIDTH / 2.0) - (GameConfig.PADDLE_INITIAL_WIDTH / 2.0);
    double playerY = GameConfig.SCREEN_HEIGHT - GameConfig.PADDLE_Y_OFFSET;
    double ballX = (GameConfig.SCREEN_WIDTH / 2.0) - (GameConfig.BALL_WIDTH / 2.0);
    double ballY = GameConfig.SCREEN_HEIGHT / 2.0;

    entityFactory.createPlayerState();
    entityFactory.createPlayer(playerX, playerY);
    entityFactory.createBall(ballX, ballY);

    // --- 5. CREATE THE GAME LOOP AND THE FINAL GAME OBJECT ---
    GameLoop gameLoop = new GameLoop(gameManager, uiManager, backgroundManager, gc);

    // Return the fully constructed and ready-to-run game instance
    return new Game(gameLoop);
  }

  /** A helper method to encapsulate the creation, registration, and wiring of all game systems. */
  private void createAndRegisterSystems(
      EventBus eventBus,
      World world,
      GraphicsContext gc,
      InputManager inputManager,
      EntityFactory entityFactory,
      EffectRegistry effectRegistry,
      GameManager gameManager,
      UIManager uiManager,
      SoundManager soundManager,
      List<EntityRenderer> renderers) {
    // --- CREATE SYSTEMS ---
    BrickSystem brickSystem = new BrickSystem(eventBus);
    ScoreSystem scoreSystem = new ScoreSystem(world, eventBus);
    LivesSystem livesSystem = new LivesSystem(world, eventBus);
    BallSystem ballSystem = new BallSystem(inputManager, entityFactory);
    BallPaddleCollisionSystem ballPaddleCollisionSystem = new BallPaddleCollisionSystem(eventBus);
    BallBrickCollisionSystem ballBrickCollisionSystem = new BallBrickCollisionSystem(eventBus);
    PaddlePowerUpCollisionSystem paddlePowerUpCollisionSystem =
        new PaddlePowerUpCollisionSystem(eventBus);
    PowerUpSystem powerUpSystem = new PowerUpSystem(world, entityFactory, effectRegistry);
    LevelCompletionSystem levelCompletionSystem = new LevelCompletionSystem(world, eventBus);

    // --- REGISTER EVENT LISTENERS ---
    eventBus.register(gameManager);
    uiManager.registerEventHandlers(eventBus);
    eventBus.register(soundManager);
    eventBus.register(brickSystem);
    eventBus.register(scoreSystem);
    eventBus.register(livesSystem);
    eventBus.register(ballSystem);
    eventBus.register(ballPaddleCollisionSystem);
    eventBus.register(ballBrickCollisionSystem);
    eventBus.register(paddlePowerUpCollisionSystem);
    eventBus.register(powerUpSystem);
    eventBus.register(levelCompletionSystem);

    // --- ADD SYSTEMS TO THE WORLD'S UPDATE LOOP ---
    world.addSystem(new RenderSystem(gc, renderers));
    world.addSystem(new MovementSystem(inputManager));
    world.addSystem(new CollisionSystem(eventBus));
    world.addSystem(new PhysicsSystem(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, eventBus));
    world.addSystem(ballSystem);
    world.addSystem(brickSystem);
    world.addSystem(scoreSystem);
    world.addSystem(livesSystem);
    world.addSystem(new EffectManagementSystem(effectRegistry));
    world.addSystem(powerUpSystem);
    world.addSystem(levelCompletionSystem);
  }
}
