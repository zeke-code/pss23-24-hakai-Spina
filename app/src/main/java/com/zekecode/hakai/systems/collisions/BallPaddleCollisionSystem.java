package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.CollisionEvent;
import com.zekecode.hakai.engine.events.PaddleHitEvent;
import com.zekecode.hakai.utils.GameConfig;

/** Handles the specific logic for when a Ball collides with the player's Paddle. */
public class BallPaddleCollisionSystem extends BallCollisionHandlerSystem {

  private final EventBus eventBus;

  public BallPaddleCollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onCollision(CollisionEvent event) {
    Entity ball = getEntityWithComponent(event, BallComponent.class);
    Entity paddle = getEntityWithComponent(event, InputComponent.class);

    // If the collision is not between a ball and a paddle, ignore it.
    if (ball == null || paddle == null) {
      return;
    }

    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();

    // Only bounce if the ball is moving downwards towards the paddle
    if (ballVelocity.y > 0) {
      resolveBounce(ball, paddle);

      // Apply paddle "spin" effect
      PositionComponent paddlePos = paddle.getComponent(PositionComponent.class).get();
      RenderComponent paddleRender = paddle.getComponent(RenderComponent.class).get();
      PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
      RenderComponent ballRender = ball.getComponent(RenderComponent.class).get();

      double paddleCenter = paddlePos.x + paddleRender.width / 2.0;
      double ballCenter = ballPos.x + ballRender.width / 2.0;
      double distanceFromCenter = ballCenter - paddleCenter;
      ballVelocity.x += distanceFromCenter * GameConfig.BALL_SPIN_FACTOR; // Apply spin

      eventBus.post(new PaddleHitEvent());
    }
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
