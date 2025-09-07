package com.zekecode.hakai.powerups.effects;

import com.zekecode.hakai.components.PaddleStateComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.powerups.Effect;
import com.zekecode.hakai.powerups.EffectId;

@EffectId("PADDLE_EXPAND")
public class PaddleExpandEffect implements Effect {

  private static final double DURATION = 10.0; // 10 seconds
  private static final double WIDTH_MULTIPLIER = 1.5;

  @Override
  public void apply(Entity target) {
    target
        .getComponent(RenderComponent.class)
        .ifPresent(
            render -> {
              // Only apply the effect if it's not already active.
              // This prevents issues if the player collects the same power-up twice.
              if (!target.hasComponent(PaddleStateComponent.class)) {
                // Store the original width in our new component.
                target.addComponent(new PaddleStateComponent(render.width));
                // Apply the width increase.
                render.width *= WIDTH_MULTIPLIER;
              }
            });
  }

  @Override
  public void remove(Entity target) {
    // Check for both components to safely revert the state.
    target
        .getComponent(RenderComponent.class)
        .ifPresent(
            render ->
                target
                    .getComponent(PaddleStateComponent.class)
                    .ifPresent(
                        paddleState -> {
                          // Restore the original width.
                          render.width = paddleState.originalWidth;
                          // Clean up by removing the state component.
                          target.removeComponent(PaddleStateComponent.class);
                        }));
  }

  @Override
  public void update(Entity target, double deltaTime) {
    // This effect does not require continuous updates.
  }

  @Override
  public double getDuration() {
    return DURATION;
  }
}
