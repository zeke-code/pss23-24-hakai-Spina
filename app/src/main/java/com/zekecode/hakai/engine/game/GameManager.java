package com.zekecode.hakai.engine.game;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.LevelClearEvent;
import com.zekecode.hakai.events.states.GameOverEvent;
import com.zekecode.hakai.ui.SceneManager;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/** Manages the high-level game state (e.g., Running, Paused, GameOver). */
public class GameManager {

  private final World world;
  private final SceneManager sceneManager;
  private GameState currentState;

  public GameManager(World world, SceneManager sceneManager) {
    this.world = world;
    this.sceneManager = sceneManager;
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
    returnToMenuAfterDelay();
  }

  /**
   * Event listener for the level clear event. Sets the game state to LEVEL_CLEAR.
   *
   * @param event - LevelClearEvent
   */
  @Subscribe
  public void onLevelClear(LevelClearEvent event) {
    setGameState(GameState.LEVEL_CLEAR);
    returnToMenuAfterDelay();
  }

  /** Returns the user to the main menu. Can be called via an input handler (e.g., ESC key). */
  public void returnToMenu() {
    sceneManager.showMainMenu();
  }

  private void returnToMenuAfterDelay() {
    PauseTransition delay = new PauseTransition(Duration.seconds(3));
    delay.setOnFinished(event -> sceneManager.showMainMenu());
    delay.play();
  }

  public void update(double deltaTime) {
    if (currentState == GameState.RUNNING) {
      world.update(deltaTime);
    }
  }

  public void togglePause() {
    if (currentState == GameState.RUNNING || currentState == GameState.PAUSED) {
      currentState = (currentState == GameState.RUNNING) ? GameState.PAUSED : GameState.RUNNING;
    }
  }

  public void setGameState(GameState newState) {
    this.currentState = newState;
    System.out.println("Game state changed to: " + newState);
  }

  public GameState getCurrentState() {
    return currentState;
  }
}
