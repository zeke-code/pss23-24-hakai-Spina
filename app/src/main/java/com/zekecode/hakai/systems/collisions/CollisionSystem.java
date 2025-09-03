package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.CollisionEvent;
import java.util.List;
import java.util.Optional;

public class CollisionSystem extends GameSystem {

  private final EventBus eventBus;

  public CollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    Optional<Entity> ballOpt = findBall(entities);
    if (ballOpt.isEmpty()) {
      return; // No ball, no collisions to check.
    }
    Entity ball = ballOpt.get();

    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();

    // Check the ball against every other entity
    for (Entity other : entities) {
      if (other.getId() == ball.getId()) {
        continue; // Don't collide with yourself
      }

      // We only care about collidable entities (those with position and render components)
      other
          .getComponent(PositionComponent.class)
          .ifPresent(
              otherPos ->
                  other
                      .getComponent(RenderComponent.class)
                      .ifPresent(
                          otherRender -> {
                            if (isColliding(ballPos, ballRender, otherPos, otherRender)) {
                              // The ONLY job is to post a generic event.
                              eventBus.post(new CollisionEvent(ball, other));
                            }
                          }));
    }
  }

  private boolean isColliding(
      PositionComponent posA,
      RenderComponent renderA,
      PositionComponent posB,
      RenderComponent renderB) {
    return posA.x < posB.x + renderB.width
        && posA.x + renderA.width > posB.x
        && posA.y < posB.y + renderB.height
        && posA.y + renderA.height > posB.y;
  }

  private Optional<Entity> findBall(List<Entity> entities) {
    for (Entity entity : entities) {
      if (entity.hasComponent(BallComponent.class)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
