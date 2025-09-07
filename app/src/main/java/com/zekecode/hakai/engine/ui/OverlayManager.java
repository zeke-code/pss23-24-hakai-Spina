package com.zekecode.hakai.engine.ui;

import com.zekecode.hakai.engine.enums.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** Manages rendering full-screen overlays like "Paused" and "Game Over". */
public class OverlayManager {

  private final double screenWidth;
  private final double screenHeight;
  private final Font overlayFont = Font.font("Arial", 72);

  public OverlayManager(double screenWidth, double screenHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  public void render(GraphicsContext gc, GameState currentState) {
    switch (currentState) {
      case PAUSED:
        drawPausedScreen(gc);
        break;
      case GAME_OVER:
        drawGameOverScreen(gc);
        break;
      default:
        // Do nothing for other states
        break;
    }
  }

  private void drawPausedScreen(GraphicsContext gc) {
    gc.setFill(Color.web("black", 0.6));
    gc.fillRect(0, 0, screenWidth, screenHeight);

    gc.save();
    gc.setEffect(new DropShadow(10, Color.BLACK));
    gc.setFont(overlayFont);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setFill(Color.WHITE);
    gc.fillText("PAUSED", screenWidth / 2, screenHeight / 2);
    gc.restore();
  }

  private void drawGameOverScreen(GraphicsContext gc) {
    gc.setFill(Color.web("black", 0.75));
    gc.fillRect(0, 0, screenWidth, screenHeight);

    gc.save();
    gc.setEffect(new DropShadow(20, Color.RED));
    gc.setFont(overlayFont);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setFill(Color.CRIMSON);
    gc.fillText("GAME OVER", screenWidth / 2, screenHeight / 2);
    gc.restore();
  }
}
