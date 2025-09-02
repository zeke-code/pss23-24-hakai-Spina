package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.BrickComponent;
import com.zekecode.hakai.components.DeadComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.BrickDestroyedEvent;
import com.zekecode.hakai.engine.events.BrickHitEvent;
import java.util.List;

public class BrickSystem extends GameSystem {

  private final EventBus eventBus;

  public BrickSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onBrickHit(BrickHitEvent event) {
    Entity brickEntity = event.brickEntity;
    brickEntity
        .getComponent(BrickComponent.class)
        .ifPresent(
            brick -> {
              brick.hp--;

              if (brick.hp <= 0) {
                // Before destroying the entity, publish the "destroyed" event.
                eventBus.post(new BrickDestroyedEvent(brickEntity));

                brickEntity.addComponent(new DeadComponent());
              }
            });
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven, so it doesn't need to do anything
    // in the main game loop.
  }
}
