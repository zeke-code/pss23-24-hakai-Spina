package com.zekecode.hakai.engine.input;

import com.zekecode.hakai.engine.game.GameManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Encapsulates the logic for handling raw keyboard input from a JavaFX Scene. It updates the
 * InputManager state and can delegate global actions to the GameManager.
 */
public class InputHandler {

  private final InputManager inputManager;
  private final GameManager gameManager;

  public InputHandler(InputManager inputManager, GameManager gameManager) {
    this.inputManager = inputManager;
    this.gameManager = gameManager;
  }

  /** Attaches the necessary key event listeners to the main game scene. */
  public void attach(Scene scene) {
    scene.setOnKeyPressed(
        event -> {
          // Update the state for game systems to use
          inputManager.pressKey(event.getCode());

          // Handle global, non-ECS actions like pausing
          if (event.getCode() == KeyCode.P) {
            gameManager.togglePause();
          }
        });

    scene.setOnKeyReleased(event -> inputManager.releaseKey(event.getCode()));
  }
}
