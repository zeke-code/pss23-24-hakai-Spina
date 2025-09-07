package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.events.CollisionEvent;
import com.zekecode.hakai.events.brick.BrickHitEvent;

/** Handles the specific logic for when a Ball collides with a Brick. */
public class BallBrickCollisionSystem extends BallCollisionHandlerSystem {

  private final EventBus eventBus;

  public BallBrickCollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onCollision(CollisionEvent event) {
    Entity ball = getEntityWithComponent(event, BallComponent.class);
    Entity brick = getEntityWithComponent(event, BrickComponent.class);

    // If the collision is not between a ball and a brick, ignore it.
    if (ball == null || brick == null) {
      return;
    }

    // Resolve the physics
    resolveBounce(ball, brick);

    // Post the gameplay event for other systems (like ScoreSystem)
    eventBus.post(new BrickHitEvent(brick));
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
}
