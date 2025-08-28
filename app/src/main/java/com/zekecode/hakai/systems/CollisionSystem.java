package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;
import java.util.Optional;

public class CollisionSystem extends GameSystem {

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // Find the ball first
    Optional<Entity> ballOpt = findBall(entities);
    if (!ballOpt.isPresent()) {
      return; // No ball in the game, nothing to do
    }
    Entity ball = ballOpt.get();

    // Check the ball against all other collidable entities
    for (Entity other : entities) {
      if (other.getId() == ball.getId()) {
        continue; // Don't check against self
      }

      // We only care about ball-paddle collision for now
      if (other.hasComponent(InputComponent.class)) {
        handleBallPaddleCollision(ball, other);
      }

      // TODO: if (other.hasComponent(BrickComponent.class)) { ... }
    }
  }

  private void handleBallPaddleCollision(Entity ball, Entity paddle) {
    // AABB Collision Check requires Position and Render components on both entities
    Optional<PositionComponent> ballPosOpt = ball.getComponent(PositionComponent.class);
    Optional<RenderComponent> ballRenderOpt = ball.getComponent(RenderComponent.class);
    Optional<PositionComponent> paddlePosOpt = paddle.getComponent(PositionComponent.class);
    Optional<RenderComponent> paddleRenderOpt = paddle.getComponent(RenderComponent.class);

    if (ballPosOpt.isPresent()
        && ballRenderOpt.isPresent()
        && paddlePosOpt.isPresent()
        && paddleRenderOpt.isPresent()) {
      PositionComponent ballPos = ballPosOpt.get();
      RenderComponent ballRender = ballRenderOpt.get();
      PositionComponent paddlePos = paddlePosOpt.get();
      RenderComponent paddleRender = paddleRenderOpt.get();

      // Check for overlap
      if (ballPos.x < paddlePos.x + paddleRender.width
          && ballPos.x + ballRender.width > paddlePos.x
          && ballPos.y < paddlePos.y + paddleRender.height
          && ballPos.y + ballRender.height > paddlePos.y) {

        ball.getComponent(VelocityComponent.class)
            .ifPresent(
                ballVelocity -> {
                  // Only trigger the bounce if the ball is moving downwards towards the paddle
                  if (ballVelocity.y > 0) {
                    ballVelocity.y *= -1; // Reverse vertical velocity

                    double paddleCenter = paddlePos.x + paddleRender.width / 2.0;
                    double ballCenter = ballPos.x + ballRender.width / 2.0;
                    double distanceFromCenter = ballCenter - paddleCenter;

                    // Adjust horizontal velocity based on where the ball hit the paddle
                    ballVelocity.x += distanceFromCenter * 2;
                  }
                });
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
