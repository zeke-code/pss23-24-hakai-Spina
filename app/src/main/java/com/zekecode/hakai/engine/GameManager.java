package com.zekecode.hakai.engine;

import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.enums.GameState;

/** Manages the high-level game state (e.g., Running, Paused, GameOver). */
public class GameManager {

  private final World world;
  private GameState currentState;

  public GameManager(World world) {
    this.world = world;
    this.currentState = GameState.RUNNING; // Default starting state
  }

  public void update(double deltaTime) {
    if (currentState == GameState.RUNNING) {
      world.update(deltaTime);
    }
  }

  public void togglePause() {
    currentState = (currentState == GameState.RUNNING) ? GameState.PAUSED : GameState.RUNNING;
  }

  public void setGameState(GameState newState) {
    this.currentState = newState;
    System.out.println("Game state changed to: " + newState);
  }

  public GameState getCurrentState() {
    return currentState;
  }
}
