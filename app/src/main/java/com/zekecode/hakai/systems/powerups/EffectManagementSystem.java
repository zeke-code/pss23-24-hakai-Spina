package com.zekecode.hakai.systems.powerups;

import com.zekecode.hakai.components.powerups.ActiveEffectComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;

/**
 * Manages the lifecycle of active, timed power-ups and maluses. It updates their timers and removes
 * them once they expire.
 */
public class EffectManagementSystem extends GameSystem {

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      entity
          .getComponent(ActiveEffectComponent.class)
          .ifPresent(
              activeEffect -> {
                // Allow the effect to perform continuous logic (e.g., visual effects)
                activeEffect.effect.update(entity, deltaTime);

                // Handle timed effects
                if (activeEffect.timeRemaining > 0) {
                  activeEffect.timeRemaining -= deltaTime;

                  if (activeEffect.timeRemaining <= 0) {
                    // Timer expired, remove the effect
                    activeEffect.effect.remove(entity);
                    entity.removeComponent(ActiveEffectComponent.class);
                  }
                }
              });
    }
  }
}
