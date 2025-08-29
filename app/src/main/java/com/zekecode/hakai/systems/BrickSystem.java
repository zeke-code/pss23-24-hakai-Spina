package com.zekecode.hakai.systems;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.BrickComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.events.BrickHitEvent;
import java.util.List;

/**
 * Manages the state and lifecycle of Brick entities. It listens for collision events and applies
 * damage, destroying bricks when their HP reaches zero.
 */
public class BrickSystem extends GameSystem {

  private final World world;

  public BrickSystem(World world) {
    this.world = world;
  }

  /**
   * This method is an event listener. Guava's EventBus will call it automatically whenever a
   * BrickDestroyedEvent is posted.
   *
   * @param event The event object containing the brick that was hit.
   */
  @Subscribe
  public void onBrickHit(BrickHitEvent event) {
    // Get the entity from the event
    Entity brickEntity = event.brickEntity;

    // Safely get the BrickComponent from the entity
    brickEntity
        .getComponent(BrickComponent.class)
        .ifPresent(
            brick -> {
              // --- THIS IS THE GAMEPLAY LOGIC ---

              // 1. Reduce the brick's HP
              brick.hp--;

              // 2. Check if the brick's HP is zero or less
              if (brick.hp <= 0) {
                // 3. If so, tell the world to destroy this entity
                world.destroyEntity(brickEntity);
              }
            });
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven, so it doesn't need to do anything
    // in the main game loop.
  }
}
