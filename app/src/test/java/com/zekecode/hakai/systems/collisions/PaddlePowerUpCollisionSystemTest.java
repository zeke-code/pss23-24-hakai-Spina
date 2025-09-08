package com.zekecode.hakai.systems.collisions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.entities.DeadComponent;
import com.zekecode.hakai.components.powerups.PowerUpDropComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.events.CollisionEvent;
import com.zekecode.hakai.events.powerup.PowerUpCollectedEvent;
import com.zekecode.hakai.powerups.EffectCategory;
import com.zekecode.hakai.powerups.PowerUpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaddlePowerUpCollisionSystemTest {

  @Mock private EventBus eventBus;

  private PaddlePowerUpCollisionSystem collisionSystem;
  private Entity paddle;
  private Entity powerUp;

  @BeforeEach
  void setUp() {
    collisionSystem = new PaddlePowerUpCollisionSystem(eventBus);

    paddle = new Entity(1);
    paddle.addComponent(new InputComponent());

    powerUp = new Entity(2);
    powerUp.addComponent(
        new PowerUpDropComponent(PowerUpType.PADDLE_EXPAND, EffectCategory.POSITIVE));
  }

  @Test
  void onCollision_withPaddleAndPowerUp_shouldMarkPowerUpDeadAndPostCollectedEvent() {
    // ARRANGE
    CollisionEvent event = new CollisionEvent(paddle, powerUp);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // 1. The power-up drop should be marked for removal from the world.
    assertTrue(
        powerUp.hasComponent(DeadComponent.class), "Power-up entity should be marked as dead.");

    // 2. The system must post an event to notify other systems of the collection.
    ArgumentCaptor<PowerUpCollectedEvent> eventCaptor =
        ArgumentCaptor.forClass(PowerUpCollectedEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // 3. The event must contain the correct data.
    PowerUpCollectedEvent postedEvent = eventCaptor.getValue();
    assertEquals(
        paddle,
        postedEvent.collectorEntity,
        "The event should identify the paddle as the collector.");
    assertEquals(
        PowerUpType.PADDLE_EXPAND,
        postedEvent.powerUpType,
        "The event should contain the correct power-up type.");
  }

  @Test
  void onCollision_withBallAndPowerUp_shouldDoNothing() {
    // ARRANGE
    // A ball cannot collect a power-up, only the paddle can.
    Entity ball = new Entity(3);
    ball.addComponent(new BallComponent());
    CollisionEvent event = new CollisionEvent(ball, powerUp);

    // ACT
    collisionSystem.onCollision(event);

    // ASSERT
    // The power-up should not be marked as dead.
    assertFalse(powerUp.hasComponent(DeadComponent.class));
    // The system should not post any events.
    verifyNoInteractions(eventBus);
  }
}
