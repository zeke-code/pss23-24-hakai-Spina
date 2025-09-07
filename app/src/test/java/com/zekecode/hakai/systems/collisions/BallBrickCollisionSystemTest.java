package com.zekecode.hakai.systems.collisions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.BrickHitEvent;
import com.zekecode.hakai.engine.events.CollisionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallBrickCollisionSystemTest {

  @Mock private EventBus eventBus;

  private BallBrickCollisionSystem collisionSystem;
  private Entity ball;
  private Entity brick;

  @BeforeEach
  void setUp() {
    collisionSystem = new BallBrickCollisionSystem(eventBus);

    ball = new Entity(1);
    ball.addComponent(new BallComponent());
    ball.addComponent(new PositionComponent(100, 100));
    ball.addComponent(new RenderComponent(15, 15, null));
    // Ball is moving down and to the right, towards the brick
    ball.addComponent(new VelocityComponent(50, 50));

    brick = new Entity(2);
    brick.addComponent(new BrickComponent(1));
    brick.addComponent(new PositionComponent(105, 105)); // Positioned for collision
    brick.addComponent(new RenderComponent(50, 20, null));
  }

  @Test
  void onCollision_withBallAndBrick_shouldBounceBallAndPostBrickHitEvent() {
    // ARRANGE
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    double initialYVelocity = ballVelocity.y;
    CollisionEvent event = new CollisionEvent(ball, brick);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // 1. Verify the physics: The ball's velocity should be reversed on the primary axis of
    // collision.
    // In this setup, it's a head-on vertical collision.
    assertEquals(
        -initialYVelocity,
        ballVelocity.y,
        "Ball's Y velocity should be reversed after hitting the brick from above.");

    // 2. Verify the gameplay event: A BrickHitEvent must be posted.
    ArgumentCaptor<BrickHitEvent> eventCaptor = ArgumentCaptor.forClass(BrickHitEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // 3. Verify the event content: The event must refer to the correct brick.
    BrickHitEvent postedEvent = eventCaptor.getValue();
    assertEquals(
        brick,
        postedEvent.brickEntity,
        "The BrickHitEvent should contain a reference to the brick that was hit.");
  }

  @Test
  void onCollision_withEntitiesOtherThanBallAndBrick_shouldDoNothing() {
    // ARRANGE
    Entity paddle = new Entity(3);
    paddle.addComponent(new InputComponent());

    // A collision between a brick and a paddle should be ignored by this system.
    CollisionEvent event = new CollisionEvent(brick, paddle);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // The system should not post any events.
    verifyNoInteractions(eventBus);
  }
}
