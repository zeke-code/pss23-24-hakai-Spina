package com.zekecode.hakai.engine;

import com.zekecode.hakai.engine.fx.BackgroundManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class GameLoop extends AnimationTimer {

  private final GameManager gameManager;
  private final UIManager uiManager;
  private final BackgroundManager backgroundManager;
  private final GraphicsContext gc;
  private long lastUpdate = 0;

  public GameLoop(
      GameManager gameManager,
      UIManager uiManager,
      BackgroundManager backgroundManager,
      GraphicsContext gc) {
    this.gameManager = gameManager;
    this.uiManager = uiManager;
    this.backgroundManager = backgroundManager;
    this.gc = gc;
  }

  @Override
  public void handle(long now) {
    if (lastUpdate == 0) {
      lastUpdate = now;
      return;
    }

    double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

    // 1. Clear the entire canvas
    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

    // 2. Render the background first
    backgroundManager.render(gc);

    // 3. Update game logic (which includes the RenderSystem drawing entities on top of the
    // background)
    gameManager.update(deltaTime);

    // 4. Render UI on top of everything else
    uiManager.render(gc, gameManager.getCurrentState());

    lastUpdate = now;
  }
}
