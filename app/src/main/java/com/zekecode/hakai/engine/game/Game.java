package com.zekecode.hakai.engine.game;

/**
 * Represents the game instance. After being constructed by the GameBuilder, this class is a simple
 * lifecycle manager responsible for running and stopping the main game loop.
 */
public class Game {

  private final GameLoop gameLoop;

  /**
   * Constructs a new Game instance.
   *
   * @param gameLoop The fully configured game loop that this instance will manage.
   */
  public Game(GameLoop gameLoop) {
    this.gameLoop = gameLoop;
  }

  /** Starts the main game loop. */
  public void run() {
    if (gameLoop == null) {
      throw new IllegalStateException("Game has not been built correctly. GameLoop is null.");
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
