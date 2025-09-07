package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.DeadComponent;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PowerUpDropComponent;
import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.CollisionEvent;
import com.zekecode.hakai.engine.events.PowerUpCollectedEvent;
import java.util.List;

/** Handles the specific collision logic between the player's paddle and a power-up drop. */
public class PaddlePowerUpCollisionSystem extends GameSystem {

  private final EventBus eventBus;

  public PaddlePowerUpCollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onCollision(CollisionEvent event) {
    Entity paddle = getEntityWithComponent(event, InputComponent.class);
    Entity powerUp = getEntityWithComponent(event, PowerUpDropComponent.class);

    if (paddle == null || powerUp == null) {
      return;
    }

    // Mark the power-up drop for removal from the game world
    powerUp.addComponent(new DeadComponent());

    // Publish an event to notify other systems that a power-up was collected
    String type = powerUp.getComponent(PowerUpDropComponent.class).get().type;
    eventBus.post(new PowerUpCollectedEvent(paddle, type));
  }

  private <T extends Component> Entity getEntityWithComponent(
      CollisionEvent event, Class<T> componentClass) {
    if (event.entityA.hasComponent(componentClass)) {
      return event.entityA;
    }
    if (event.entityB.hasComponent(componentClass)) {
      return event.entityB;
    }
    return null;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven.
  }
}
