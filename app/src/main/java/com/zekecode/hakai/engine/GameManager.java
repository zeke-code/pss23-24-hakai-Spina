package com.zekecode.hakai.engine;

import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.enums.GameState;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.systems.CollisionSystem;
import com.zekecode.hakai.systems.MovementSystem;
import com.zekecode.hakai.systems.PhysicsSystem;
import com.zekecode.hakai.systems.RenderSystem;
import javafx.scene.canvas.GraphicsContext;

/** Manages the high-level game state (e.g., Running, Paused) and the ECS World. */
public class GameManager {

  private final World world;
  private final EntityFactory entityFactory;
  private GameState currentState;

  public GameManager() {
    this.world = new World();
    this.entityFactory = new EntityFactory(world);
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
    double playerX = screenWidth / 2.0 - 50;
    double playerY = screenHeight - 50;
    entityFactory.createPlayer(playerX, playerY);

    // Create the ball above the paddle
    double ballX = screenWidth / 2.0 - 5; // Center the ball
    double ballY = screenHeight / 2.0;
    entityFactory.createBall(200, -200); // Start it moving up and to the right
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
