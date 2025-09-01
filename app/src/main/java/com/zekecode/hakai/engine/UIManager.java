package com.zekecode.hakai.engine;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.engine.enums.GameState;
import com.zekecode.hakai.engine.events.LivesChangedEvent;
import com.zekecode.hakai.engine.events.ScoreChangedEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Manages the rendering of all User Interface elements, such as menus, scores, and overlays. It is
 * separate from the game world's RenderSystem and is updated via the EventBus.
 */
public class UIManager {

  private final double screenWidth;
  private final double screenHeight;

  private int currentScore = 0;
  private int currentLives = 0;

  private final Font hudFont = Font.font("Arial", FontWeight.BOLD, 22);
  private final Font overlayFont = Font.font("Arial", FontWeight.BOLD, 72);

  public UIManager(double screenWidth, double screenHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  // --- Event Subscribers ---

  @Subscribe
  public void onScoreChanged(ScoreChangedEvent event) {
    this.currentScore = event.newScore;
  }

  @Subscribe
  public void onLivesChanged(LivesChangedEvent event) {
    this.currentLives = event.remainingLives;
  }

  /**
   * Renders the appropriate UI based on the current game state. This is called every frame after
   * the world has been rendered.
   */
  public void render(GraphicsContext gc, GameState currentState) {
    // The HUD is always drawn, except when the game is over.
    if (currentState != GameState.GAME_OVER) {
      drawGameHUD(gc);
    }

    // Overlays are drawn on top of the HUD
    switch (currentState) {
      case PAUSED:
        drawPausedScreen(gc);
        break;
      case GAME_OVER:
        drawGameOverScreen(gc);
        break;
      default:
        break;
    }
  }

  private void drawGameHUD(GraphicsContext gc) {
    // Apply a drop shadow effect to make text and icons pop
    gc.save(); // Save the current state
    gc.setEffect(new DropShadow(5, Color.BLACK));

    gc.setFont(hudFont);
    gc.setFill(Color.WHITE);

    // --- Draw Score ---
    gc.setTextAlign(TextAlignment.LEFT);
    gc.fillText("SCORE", 20, 35);
    gc.fillText(String.format("%04d", currentScore), 20, 60);

    // --- Draw Lives as Icons ---
    double iconWidth = 30;
    double iconHeight = 8;
    double padding = 10;
    for (int i = 0; i < currentLives; i++) {
      double x = screenWidth - 40 - (i * (iconWidth + padding));
      gc.fillRoundRect(x, 25, iconWidth, iconHeight, 8, 8);
    }

    gc.restore(); // Restore the state to remove the shadow effect for other drawings
  }

  private void drawPausedScreen(GraphicsContext gc) {
    // Semi-transparent overlay
    gc.setFill(Color.web("black", 0.6));
    gc.fillRect(0, 0, screenWidth, screenHeight);

    // Paused text with shadow
    gc.save();
    gc.setEffect(new DropShadow(10, Color.BLACK));
    gc.setFont(overlayFont);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setFill(Color.WHITE);
    gc.fillText("PAUSED", screenWidth / 2, screenHeight / 2);
    gc.restore();
  }

  private void drawGameOverScreen(GraphicsContext gc) {
    // Semi-transparent overlay
    gc.setFill(Color.web("black", 0.75));
    gc.fillRect(0, 0, screenWidth, screenHeight);

    // "GAME OVER" text with a red glow effect (via shadow)
    gc.save();
    gc.setEffect(new DropShadow(20, Color.RED));
    gc.setFont(overlayFont);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setFill(Color.CRIMSON);
    gc.fillText("GAME OVER", screenWidth / 2, screenHeight / 2);
    gc.restore();
  }
}
