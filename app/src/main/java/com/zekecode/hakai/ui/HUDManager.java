package com.zekecode.hakai.ui;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.events.LivesChangedEvent;
import com.zekecode.hakai.events.ScoreChangedEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/** Manages rendering the Heads-Up Display (Score, Lives, etc.) during active gameplay. */
public class HUDManager {

  private final double screenWidth;
  private int currentScore = 0;
  private int currentLives = 0;
  private final Font hudFont;

  public HUDManager(double screenWidth) {
    this.screenWidth = screenWidth;
    this.hudFont = Font.font("Arial", FontWeight.BOLD, 22);
  }

  @Subscribe
  public void onScoreChanged(ScoreChangedEvent event) {
    this.currentScore = event.newScore;
  }

  @Subscribe
  public void onLivesChanged(LivesChangedEvent event) {
    this.currentLives = event.remainingLives;
  }

  public void render(GraphicsContext gc) {
    Color panelColor = Color.web("black", 0.4);
    Color textColor = Color.WHITE;
    double padding = 15;

    gc.setFont(hudFont);

    // --- Draw Score Panel ---
    gc.setFill(panelColor);
    gc.fillRoundRect(padding, padding, 220, 80, 20, 20);
    gc.setFill(textColor);
    gc.setTextAlign(TextAlignment.LEFT);
    gc.fillText("SCORE", padding * 2, padding + 30);
    gc.fillText(String.format("%06d", currentScore), padding * 2, padding + 60);

    // --- Draw Lives Panel ---
    double livesPanelWidth = 180;
    gc.setFill(panelColor);
    gc.fillRoundRect(screenWidth - livesPanelWidth - padding, padding, livesPanelWidth, 80, 20, 20);
    gc.setFill(textColor);
    gc.fillText("LIVES", screenWidth - livesPanelWidth, padding + 30);

    // Draw paddle icons
    for (int i = 0; i < currentLives; i++) {
      drawLifeIcon(gc, i, padding);
    }
  }

  private void drawLifeIcon(GraphicsContext gc, int index, double padding) {
    double iconWidth = 35;
    double iconHeight = 8;
    double iconPadding = 10;
    double x = screenWidth - (padding * 2) - iconWidth - (index * (iconWidth + iconPadding));
    double y = padding + 48;

    gc.setFill(Color.LIGHTGRAY);
    gc.fillRoundRect(x, y, iconWidth, iconHeight, 5, 5);
    gc.setStroke(Color.WHITE);
    gc.strokeRoundRect(x, y, iconWidth, iconHeight, 5, 5);
  }
}
