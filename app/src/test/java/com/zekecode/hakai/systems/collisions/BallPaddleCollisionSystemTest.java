package com.zekecode.hakai.systems.collisions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.CollisionEvent;
import com.zekecode.hakai.engine.events.PaddleHitEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallPaddleCollisionSystemTest {

  @Mock private EventBus eventBus;

  private BallPaddleCollisionSystem collisionSystem;
  private Entity paddle;
  private Entity ball;

  @BeforeEach
  void setUp() {
    collisionSystem = new BallPaddleCollisionSystem(eventBus);

    paddle = new Entity(1);
    paddle.addComponent(new InputComponent());
    paddle.addComponent(new PositionComponent(350, 550));
    paddle.addComponent(new RenderComponent(100, 20, null));

    ball = new Entity(2);
    ball.addComponent(new BallComponent());
    ball.addComponent(new PositionComponent(392.5, 535)); // Centered on paddle
    ball.addComponent(new VelocityComponent(20, 150)); // Moving down and right
    ball.addComponent(new RenderComponent(15, 15, null));
  }

  @Test
  void onCollision_whenBallHitsPaddleFromAbove_shouldBounceUpAndPostEvent() {
    // ARRANGE
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    assertTrue(ballVelocity.y > 0, "Pre-condition: Ball must be moving downwards.");
    CollisionEvent event = new CollisionEvent(ball, paddle);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // The ball's vertical velocity should be reversed.
    assertTrue(ballVelocity.y < 0, "Ball's Y velocity should be negative (upwards) after bounce.");

    // A PaddleHitEvent should be posted for sound effects, etc.
    ArgumentCaptor<PaddleHitEvent> eventCaptor = ArgumentCaptor.forClass(PaddleHitEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());
    assertTrue(eventCaptor.getValue() instanceof PaddleHitEvent);
  }

  @Test
  void onCollision_whenBallHitsRightOfPaddleCenter_shouldIncreasePositiveXVelocity() {
    // ARRANGE
    // Move ball to the right side of the paddle
    ball.getComponent(PositionComponent.class).get().x = 420; // Paddle center is 400
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    double initialXVelocity = ballVelocity.x;
    CollisionEvent event = new CollisionEvent(ball, paddle);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // The ball's horizontal velocity should increase due to "spin".
    assertTrue(
        ballVelocity.x > initialXVelocity,
        "X velocity should increase when hitting the right side of the paddle.");
  }

  @Test
  void onCollision_whenBallHitsLeftOfPaddleCenter_shouldIncreaseNegativeXVelocity() {
    // ARRANGE
    // Move ball to the left side of the paddle
    ball.getComponent(PositionComponent.class).get().x = 360; // Paddle center is 400
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    double initialXVelocity = ballVelocity.x; // Is 20
    CollisionEvent event = new CollisionEvent(ball, paddle);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // The ball's horizontal velocity should decrease (become more negative or less positive).
    assertTrue(
        ballVelocity.x < initialXVelocity,
        "X velocity should decrease when hitting the left side of the paddle.");
  }
}
