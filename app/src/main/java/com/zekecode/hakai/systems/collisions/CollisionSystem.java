package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.CollidableComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.CollisionEvent;
import java.util.List;

public class CollisionSystem extends GameSystem {

  private final EventBus eventBus;

  public CollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // 1. Pre-filter the entity list into more specific groups for efficiency.
    List<Entity> balls =
        entities.stream().filter(e -> e.hasComponent(BallComponent.class)).toList();

    List<Entity> collidables =
        entities.stream().filter(e -> e.hasComponent(CollidableComponent.class)).toList();

    // If there are no balls or nothing to collide with, no need to proceed.
    if (balls.isEmpty() || collidables.isEmpty()) {
      return;
    }

    // 2. Check every ball against every collidable entity.
    for (Entity ball : balls) {
      // It's safe to .get() these components because a ball will always have them.
      PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
      RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();

      for (Entity other : collidables) {
        // A collidable entity might be the ball itself, so we must prevent self-collision checks.
        if (other.getId() == ball.getId()) {
          continue;
        }

        // We can be confident these components exist because CollidableComponent
        // is only added to renderable entities.
        other
            .getComponent(PositionComponent.class)
            .ifPresent(
                otherPos ->
                    other
                        .getComponent(RenderComponent.class)
                        .ifPresent(
                            otherRender -> {
                              if (isColliding(ballPos, ballRender, otherPos, otherRender)) {
                                eventBus.post(new CollisionEvent(ball, other));
                              }
                            }));
      }
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
}
