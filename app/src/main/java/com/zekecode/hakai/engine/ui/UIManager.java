package com.zekecode.hakai.engine.ui;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.engine.game.GameState;
import javafx.scene.canvas.GraphicsContext;

/**
 * The main coordinator for the UI layer. It holds references to specialized UI managers and calls
 * their render methods in the correct order. It also registers them with the EventBus.
 */
public class UIManager {

  private final HUDManager hudManager;
  private final OverlayManager overlayManager;

  public UIManager(double screenWidth, double screenHeight) {
    this.hudManager = new HUDManager(screenWidth);
    this.overlayManager = new OverlayManager(screenWidth, screenHeight);
  }

  /** Registers all underlying UI managers with the event bus. */
  public void registerEventHandlers(EventBus eventBus) {
    eventBus.register(hudManager);
  }

  /** Renders all UI components in the correct order. */
  public void render(GraphicsContext gc, GameState currentState) {
    // HUD is drawn unless the game is over.
    if (currentState != GameState.GAME_OVER) {
      hudManager.render(gc);
    }

    // Overlays are drawn on top of everything.
    overlayManager.render(gc, currentState);
  }
}
