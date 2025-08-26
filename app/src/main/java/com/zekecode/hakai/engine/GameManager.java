package com.zekecode.hakai.engine;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.enums.GameState;
import com.zekecode.hakai.systems.RenderSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameManager {

  private final World world;
  private GameState currentState;
  private final GraphicsContext gc;
  private final double screenWidth;
  private final double screenHeight;

  public GameManager(GraphicsContext gc, double screenWidth, double screenHeight) {
    this.world = new World();
    this.currentState = GameState.RUNNING; // Start the game in a running state
    this.gc = gc;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;

    setupSystems();
    setupEntities();
  }

  private void setupSystems() {
    world.addSystem(new RenderSystem(gc));
    // We will add MovementSystem, CollisionSystem etc. here later
  }

  private void setupEntities() {
    // Create the player paddle
    Entity player = world.createEntity();
    double playerX = screenWidth / 2.0 - 50;
    double playerY = screenHeight - 50;
    player.addComponent(new PositionComponent(playerX, playerY));
    player.addComponent(new RenderComponent(100, 20, Color.BLUE));
  }

  // This is the main update method called by the game loop
  public void update(double deltaTime) {
    if (currentState == GameState.RUNNING) {
      world.update(deltaTime); // This will call update on all systems
    }

    // We can draw UI elements based on state
    renderUI();
  }

  private void renderUI() {
    if (currentState == GameState.PAUSED) {
      gc.setFill(Color.web("white", 0.8));
      gc.fillRect(0, 0, screenWidth, screenHeight);

      gc.setFill(Color.BLACK);
      gc.setFont(new Font("Arial", 50));
      gc.setTextAlign(TextAlignment.CENTER);
      gc.fillText("PAUSED", screenWidth / 2, screenHeight / 2);
    }
  }

  public void togglePause() {
    currentState = (currentState == GameState.RUNNING) ? GameState.PAUSED : GameState.RUNNING;
  }
}
