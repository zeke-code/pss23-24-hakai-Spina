package com.zekecode.hakai.systems.powerups;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.powerups.ActiveEffectsComponent;
import com.zekecode.hakai.components.powerups.PowerUpComponent;
import com.zekecode.hakai.config.data.PowerUpData;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import com.zekecode.hakai.events.powerup.PowerUpCollectedEvent;
import com.zekecode.hakai.powerups.EffectCategory;
import com.zekecode.hakai.powerups.EffectRegistry;
import com.zekecode.hakai.powerups.PowerUpType;
import java.util.List;
import java.util.Optional;

/**
 * The central system for handling all power-up and malus logic. It listens for game events to
 * trigger effects, either instantly or by spawning collectible drops.
 */
public class PowerUpSystem extends GameSystem {

  private final World world;
  private final EntityFactory entityFactory;
  private final EffectRegistry effectRegistry;

  public PowerUpSystem(World world, EntityFactory entityFactory, EffectRegistry effectRegistry) {
    this.world = world;
    this.entityFactory = entityFactory;
    this.effectRegistry = effectRegistry;
  }

  /** Handles the destruction of a brick, checking if it should trigger an effect. */
  @Subscribe
  public void onBrickDestroyed(BrickDestroyedEvent event) {
    event
        .brickEntity
        .getComponent(PowerUpComponent.class)
        .ifPresent(
            powerUpComp -> {
              PowerUpData data = powerUpComp.powerUpData;
              switch (data.trigger) {
                case INSTANT:
                  applyEffect(data.type, event.brickEntity);
                  break;
                case ON_COLLECT:
                  effectRegistry
                      .getEffect(data.type)
                      .ifPresent(
                          effect -> {
                            EffectCategory category = effect.getCategory();
                            event
                                .brickEntity
                                .getComponent(PositionComponent.class)
                                .ifPresent(
                                    pos ->
                                        entityFactory.createPowerUpDrop(
                                            pos.x, pos.y, data.type, category));
                          });
                  break;
              }
            });
  }

  /** Handles the collection of a power-up drop by the paddle. */
  @Subscribe
  public void onPowerUpCollected(PowerUpCollectedEvent event) {
    applyEffect(event.powerUpType, event.collectorEntity);
  }

  /**
   * Applies a specific effect to a target entity. If the effect has a duration, it will add an
   * {@link ActiveEffectsComponent} to manage its lifecycle.
   *
   * @param effectType The type of effect to apply.
   * @param target The entity to apply the effect to.
   */
  private void applyEffect(PowerUpType effectType, Entity target) {
    effectRegistry
        .getEffect(effectType)
        .ifPresentOrElse(
            effect -> {
              effect.apply(target);

              if (effect.getDuration() > 0) {
                // Get the component, or create a new one if it doesn't exist.
                ActiveEffectsComponent effectsComp =
                    target
                        .getComponent(ActiveEffectsComponent.class)
                        .orElseGet(
                            () -> {
                              ActiveEffectsComponent newComp = new ActiveEffectsComponent();
                              target.addComponent(newComp);
                              return newComp;
                            });

                // Add or reset the timer for this specific effect in the map.
                effectsComp.activeEffects.put(effectType, effect.getDuration());
              }
              System.out.println("Applied effect '" + effectType + "' to entity " + target.getId());
            },
            () ->
                System.err.println("Attempted to apply unknown effect type: '" + effectType + "'"));
  }

  private Optional<Entity> findPlayerPaddle() {
    return world.getEntities().stream()
        .filter(e -> e.hasComponent(InputComponent.class))
        .findFirst();
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven.
  }
}
