package com.zekecode.hakai.systems.powerups;

import com.zekecode.hakai.components.powerups.ActiveEffectsComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.powerups.EffectRegistry;
import com.zekecode.hakai.powerups.PowerUpType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A system dedicated to managing the lifecycle of active effects on entities. It updates effect
 * timers, applies continuous effect logic, and cleans up expired effects.
 */
public class EffectManagementSystem extends GameSystem {

  private final EffectRegistry effectRegistry;

  public EffectManagementSystem(EffectRegistry effectRegistry) {
    this.effectRegistry = effectRegistry;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      entity
          .getComponent(ActiveEffectsComponent.class)
          .ifPresent(
              effectsComp -> {
                Set<PowerUpType> expiredEffects = new HashSet<>();

                // Iterate over the map of active effects for this entity
                effectsComp.activeEffects.forEach(
                    (type, timeRemaining) -> {
                      // Allow the effect to perform continuous logic
                      effectRegistry
                          .getEffect(type)
                          .ifPresent(effect -> effect.update(entity, deltaTime));

                      // Decrement the timer
                      double newTime = timeRemaining - deltaTime;
                      if (newTime <= 0) {
                        expiredEffects.add(type);
                      } else {
                        effectsComp.activeEffects.put(type, newTime);
                      }
                    });

                // Clean up all expired effects for this entity
                for (PowerUpType expiredType : expiredEffects) {
                  effectRegistry.getEffect(expiredType).ifPresent(effect -> effect.remove(entity));
                  effectsComp.activeEffects.remove(expiredType);
                  System.out.println(
                      "Removed expired effect '" + expiredType + "' from entity " + entity.getId());
                }

                // If the component has no more active effects, remove it entirely
                if (effectsComp.activeEffects.isEmpty()) {
                  entity.removeComponent(ActiveEffectsComponent.class);
                }
              });
    }
  }
}
