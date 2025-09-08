package com.zekecode.hakai.powerups.effects;

import com.zekecode.hakai.components.paddle.PaddleSlowComponent;
import com.zekecode.hakai.components.physics.MovableComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.powerups.Effect;
import com.zekecode.hakai.powerups.EffectCategory;
import com.zekecode.hakai.powerups.EffectId;

/**
 * An effect that temporarily reduces the paddle's movement speed.
 *
 * <p>When applied, this effect halves the paddle's speed for a duration of 8 seconds. If the paddle
 * is already slowed, the effect will not stack or reset the duration.
 *
 * <p>This effect is intended to be used as a negative power-up (malus) to increase game difficulty.
 */
@EffectId("PADDLE_SLOW")
public class PaddleSlowEffect implements Effect {

  private static final double DURATION = 8.0; // 8 seconds
  private static final double SPEED_MULTIPLIER = 0.5; // Halve the speed

  @Override
  public void apply(Entity target) {
    target
        .getComponent(MovableComponent.class)
        .ifPresent(
            movable -> {
              // Only apply if not already slowed
              if (!target.hasComponent(PaddleSlowComponent.class)) {
                target.addComponent(new PaddleSlowComponent(movable.speed));
                movable.speed *= SPEED_MULTIPLIER;
              }
            });
  }

  @Override
  public void remove(Entity target) {
    target
        .getComponent(MovableComponent.class)
        .ifPresent(
            movable ->
                target
                    .getComponent(PaddleSlowComponent.class)
                    .ifPresent(
                        slowState -> {
                          movable.speed = slowState.originalSpeed;
                          target.removeComponent(PaddleSlowComponent.class);
                        }));
  }

  @Override
  public void update(Entity target, double deltaTime) {
    // This malus does not require continuous updates.
  }

  @Override
  public double getDuration() {
    return DURATION;
  }

  @Override
  public EffectCategory getCategory() {
    return EffectCategory.NEGATIVE;
  }
}
