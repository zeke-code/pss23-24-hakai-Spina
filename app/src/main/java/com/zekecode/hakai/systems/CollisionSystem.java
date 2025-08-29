package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.BrickComponent;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import java.util.List;
import java.util.Optional;

public class CollisionSystem extends GameSystem {

  private final World world;

  public CollisionSystem(World world) {
    this.world = world;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    Optional<Entity> ballOpt = findBall(entities);
    if (!ballOpt.isPresent()) {
      return; // No ball, no collisions to check.
    }
    Entity ball = ballOpt.get();

    // Check the ball against every other entity
    for (Entity other : entities) {
      if (other.getId() == ball.getId()) {
        continue; // Don't collide with yourself
      }

      // Check for different collision types
      if (other.hasComponent(InputComponent.class)) {
        handleBallPaddleCollision(ball, other);
      } else if (other.hasComponent(BrickComponent.class)) {
        handleBallBrickCollision(ball, other);
      }
    }
  }

  private void handleBallBrickCollision(Entity ball, Entity brick) {
    // We can use .get() here because we know our entities will have these components
    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();
    PositionComponent brickPos = brick.getComponent(PositionComponent.class).get();
    RenderComponent brickRender = brick.getComponent(RenderComponent.class).get();

    if (isColliding(ballPos, ballRender, brickPos, brickRender)) {
      // --- COLLISION DETECTED ---

      // 1. Damage and potentially destroy the brick
      BrickComponent brickComp = brick.getComponent(BrickComponent.class).get();
      brickComp.hp--;
      if (brickComp.hp <= 0) {
        world.destroyEntity(brick); // Tell the world to remove this entity
      }

      // 2. Make the ball bounce realistically
      resolveBounce(ball, brickPos, brickRender);
    }
  }

  private void resolveBounce(
      Entity ball, PositionComponent targetPos, RenderComponent targetRender) {
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();

    // Calculate the overlap between ball and target
    double overlapX = (ballPos.x + ballRender.width / 2) - (targetPos.x + targetRender.width / 2);
    double overlapY = (ballPos.y + ballRender.height / 2) - (targetPos.y + targetRender.height / 2);

    // Calculate the minimum non-overlapping distances
    double halfWidths = (ballRender.width + targetRender.width) / 2;
    double halfHeights = (ballRender.height + targetRender.height) / 2;

    // Determine which axis has the smallest penetration
    // This tells us if it was a vertical or horizontal collision
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

  private void handleBallPaddleCollision(Entity ball, Entity paddle) {
    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();
    PositionComponent paddlePos = paddle.getComponent(PositionComponent.class).get();
    RenderComponent paddleRender = paddle.getComponent(RenderComponent.class).get();

    if (isColliding(ballPos, ballRender, paddlePos, paddleRender)) {
      VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
      if (ballVelocity.y > 0) { // Only bounce if moving down
        resolveBounce(ball, paddlePos, paddleRender);

        // Add paddle "spin"
        double paddleCenter = paddlePos.x + paddleRender.width / 2.0;
        double ballCenter = ballPos.x + ballRender.width / 2.0;
        double distanceFromCenter = ballCenter - paddleCenter;
        ballVelocity.x += distanceFromCenter * 2;
      }
    }
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
