package com.zekecode.hakai.systems.powerups;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.PaddleExpansionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.PowerUpCollectedEvent;
import java.util.List;

/** Manages the application and duration of active power-up effects on entities. */
public class PowerUpEffectSystem extends GameSystem {

  /**
   * Listens for when a power-up is collected and applies the corresponding effect.
   *
   * @param event The PowerUpCollectedEvent.
   */
  @Subscribe
  public void onPowerUpCollected(PowerUpCollectedEvent event) {
    if ("PADDLE_SIZE_INCREASE".equals(event.powerUpType)) {
      Entity paddle = event.collectorEntity;
      // If the power-up is already active, just reset its timer.
      // Otherwise, apply the effect and add the state component.
      paddle
          .getComponent(PaddleExpansionComponent.class)
          .ifPresentOrElse(
              powerUp -> powerUp.timeRemaining = PaddleExpansionComponent.DURATION,
              () -> {
                paddle
                    .getComponent(RenderComponent.class)
                    .ifPresent(
                        render -> {
                          paddle.addComponent(new PaddleExpansionComponent(render.width));
                          render.width *= PaddleExpansionComponent.WIDTH_MULTIPLIER;
                        });
              });
    }
  }

  /**
   * Iterates through all entities with active power-ups and decrements their timers, removing the
   * effect when the timer expires.
   *
   * @param entities The list of all entities in the world.
   * @param deltaTime The time since the last frame.
   */
  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      entity
          .getComponent(PaddleExpansionComponent.class)
          .ifPresent(
              powerUp -> {
                powerUp.timeRemaining -= deltaTime;

                if (powerUp.timeRemaining <= 0) {
                  // Power-up has expired; revert the paddle's width.
                  entity
                      .getComponent(RenderComponent.class)
                      .ifPresent(render -> render.width = powerUp.originalWidth);
                  entity.removeComponent(PaddleExpansionComponent.class);
                }
              });
    }
  }
}
