package com.zekecode.hakai.systems.collisions;

import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.physics.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;

/**
 * An abstract base system that contains common collision resolution logic (like bouncing) to be
 * shared by more specific collision handling systems.
 */
public abstract class BallCollisionHandlerSystem extends GameSystem {

  /**
   * Reverses the ball's velocity based on the axis of collision.
   *
   * @param ball The ball entity.
   * @param target The entity the ball collided with.
   */
  protected void resolveBounce(Entity ball, Entity target) {
    // These .get() calls are safe within the context of a collision event.
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();
    PositionComponent targetPos = target.getComponent(PositionComponent.class).get();
    RenderComponent targetRender = target.getComponent(RenderComponent.class).get();

    // Calculate the overlap between ball and target
    double overlapX = (ballPos.x + ballRender.width / 2) - (targetPos.x + targetRender.width / 2);
    double overlapY = (ballPos.y + ballRender.height / 2) - (targetPos.y + targetRender.height / 2);

    // Calculate the minimum non-overlapping distances
    double halfWidths = (ballRender.width + targetRender.width) / 2;
    double halfHeights = (ballRender.height + targetRender.height) / 2;

    // Determine which axis has the smallest penetration
    double diffX = halfWidths - Math.abs(overlapX);
    double diffY = halfHeights - Math.abs(overlapY);

    if (diffX < diffY) { // Horizontal collision
      ballVelocity.x *= -1;
      // Reposition ball to prevent sticking
      ballPos.x += (overlapX > 0 ? diffX : -diffX);
    } else { // Vertical collision
      ballVelocity.y *= -1;
      // Reposition ball to prevent sticking
      ballPos.y += (overlapY > 0 ? diffY : -diffY);
    }
  }

  /** These systems are purely event-driven, so their update loop is empty. */
  @Override
  public void update(List<Entity> entities, double deltaTime) {}
}
