package com.zekecode.hakai.engine;

import com.zekecode.hakai.engine.enums.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Manages the rendering of all User Interface elements, such as menus, scores, and overlays. It is
 * separate from the game world's RenderSystem.
 */
public class UIManager {

  private final double screenWidth;
  private final double screenHeight;

  public UIManager(double screenWidth, double screenHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  /**
   * Renders the appropriate UI based on the current game state. This is called every frame after
   * the world has been rendered.
   */
  public void render(GraphicsContext gc, GameState currentState) {
    switch (currentState) {
      case PAUSED:
        drawPausedScreen(gc);
        break;
      case RUNNING:
        // In the future, we can draw the score, lives, etc. here
        // drawGameHUD(gc);
        break;
      case GAME_OVER:
        // drawGameOverScreen(gc);
        break;
      default:
        break;
    }
  }

  private void drawPausedScreen(GraphicsContext gc) {
    gc.setFill(Color.web("black", 0.6));
    gc.fillRect(0, 0, screenWidth, screenHeight);

    gc.setFill(Color.WHITE);
    gc.setFont(new Font("Arial", 50));
    gc.setTextAlign(TextAlignment.CENTER);
    gc.fillText("PAUSED", screenWidth / 2, screenHeight / 2);
  }

  // Example for later:
  // private void drawGameHUD(GraphicsContext gc, int score, int lives) { ... }
}
