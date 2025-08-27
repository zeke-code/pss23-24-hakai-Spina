package com.zekecode.hakai.engine;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;


public class GameLoop extends AnimationTimer {

    private final GameManager gameManager;
    private final UIManager uiManager;
    private final GraphicsContext gc;
    private long lastUpdate = 0;

    public GameLoop(GameManager gameManager, UIManager uiManager, GraphicsContext gc) {
        this.gameManager = gameManager;
        this.uiManager = uiManager;
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

        // 1. Update game logic
        gameManager.update(deltaTime);

        // 2. Render UI (the RenderSystem already ran inside gameManager.update)
        // The UIManager draws on top of what the RenderSystem drew.
        uiManager.render(gc, gameManager.getCurrentState());

        lastUpdate = now;
    }
}