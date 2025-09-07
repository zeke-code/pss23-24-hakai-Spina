package com.zekecode.hakai.engine.game;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.LevelClearEvent;
import com.zekecode.hakai.events.states.GameOverEvent;

/** Manages the high-level game state (e.g., Running, Paused, GameOver). */
public class GameManager {

  private final World world;
  private GameState currentState;

  public GameManager(World world) {
    this.world = world;
    this.currentState = GameState.RUNNING; // Default starting state
  }

  /**
   * Event listener for the game over event. Sets the game state to GAME_OVER.
   *
   * @param event - GameOverEvent
   */
  @Subscribe
  public void onGameOver(GameOverEvent event) {
    setGameState(GameState.GAME_OVER);
  }

  /**
   * Event listener for the level clear event. Sets the game state to LEVEL_CLEAR.
   *
   * @param event - LevelClearEvent
   */
  @Subscribe
  public void onLevelClear(LevelClearEvent event) {
    setGameState(GameState.LEVEL_CLEAR);
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
