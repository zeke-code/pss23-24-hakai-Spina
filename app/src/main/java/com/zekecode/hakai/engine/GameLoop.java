package com.zekecode.hakai.engine;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

  private final GameManager gameManager;
  private long lastUpdate = 0;

  public GameLoop(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  /**
   * This method is called by the JavaFX runtime on every frame. It calculates the time elapsed
   * since the last frame (deltaTime) and calls the main update method in the GameManager.
   */
  @Override
  public void handle(long now) {
    // First frame initialization
    if (lastUpdate == 0) {
      lastUpdate = now;
      return;
    }

    // Time elapsed since the last frame, in seconds.
    double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

    // Delegate the update logic to the GameManager
    gameManager.update(deltaTime);

    // Update the last update time for the next frame
    lastUpdate = now;
  }
}
