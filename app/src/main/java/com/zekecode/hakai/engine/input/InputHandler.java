package com.zekecode.hakai.engine.input;

import com.zekecode.hakai.engine.game.GameManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * InputHandler sets up key event listeners on the main game scene and updates the InputManager
 * accordingly. It also handles global actions like pausing the game or returning to the main menu.
 */
public class InputHandler {

  private final InputManager inputManager;
  private final GameManager gameManager;

  public InputHandler(InputManager inputManager, GameManager gameManager) {
    this.inputManager = inputManager;
    this.gameManager = gameManager;
  }

  /** Attaches key event listeners to the provided JavaFX scene. */
  public void attach(Scene scene) {
    scene.setOnKeyPressed(
        event -> {
          // Update the state for game systems to use
          inputManager.pressKey(event.getCode());

          // Handle global, non-ECS actions
          if (event.getCode() == KeyCode.P) {
            gameManager.togglePause();
          }
          if (event.getCode() == KeyCode.ESCAPE) {
            gameManager.returnToMenu();
          }
        });

    scene.setOnKeyReleased(event -> inputManager.releaseKey(event.getCode()));
  }
}
