package com.zekecode.hakai.systems.powerups;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.powerups.ActiveEffectComponent;
import com.zekecode.hakai.components.powerups.PowerUpComponent;
import com.zekecode.hakai.config.data.PowerUpData;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import com.zekecode.hakai.events.powerup.PowerUpCollectedEvent;
import com.zekecode.hakai.powerups.EffectRegistry;
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
                  // For instant effects, the effect is applied using the brick as context.
                  applyEffect(data.type, event.brickEntity);
                  break;
                case ON_COLLECT:
                  // Spawn a collectible drop at the brick's location.
                  event
                      .brickEntity
                      .getComponent(PositionComponent.class)
                      .ifPresent(pos -> entityFactory.createPowerUpDrop(pos.x, pos.y, data.type));
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
   * {@link ActiveEffectComponent} to manage its lifecycle.
   *
   * @param effectType The string ID of the effect to apply.
   * @param target The entity to apply the effect to.
   */
  private void applyEffect(String effectType, Entity target) {
    effectRegistry
        .getEffect(effectType)
        .ifPresentOrElse(
            effect -> {
              // Apply the effect's logic to the target entity.
              effect.apply(target);

              // If the effect is timed, add the component to manage its duration.
              if (effect.getDuration() > 0) {
                // If the same effect is already active, just reset its timer.
                // Otherwise, add a new component.
                target
                    .getComponent(ActiveEffectComponent.class)
                    .ifPresentOrElse(
                        activeEffect -> {
                          if (activeEffect.effect.getClass() == effect.getClass()) {
                            activeEffect.timeRemaining = effect.getDuration();
                          }
                        },
                        () -> target.addComponent(new ActiveEffectComponent(effect)));
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
