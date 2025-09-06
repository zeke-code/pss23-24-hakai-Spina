package com.zekecode.hakai.systems.powerups;

import static org.junit.jupiter.api.Assertions.*;

import com.zekecode.hakai.components.PaddleExpansionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.PowerUpCollectedEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PowerUpEffectSystemTest {

  private PowerUpEffectSystem powerUpEffectSystem;
  private Entity paddle;
  private RenderComponent paddleRender;
  private static final double ORIGINAL_WIDTH = 100.0;

  @BeforeEach
  void setUp() {
    powerUpEffectSystem = new PowerUpEffectSystem();

    // Setup a player paddle entity
    paddle = new Entity(1);
    paddleRender = new RenderComponent(ORIGINAL_WIDTH, 20, null);
    paddle.addComponent(paddleRender);
  }

  @Test
  void onPowerUpCollected_appliesPaddleExpansionEffect() {
    // ARRANGE
    PowerUpCollectedEvent event = new PowerUpCollectedEvent(paddle, "PADDLE_SIZE_INCREASE");

    // ACT
    powerUpEffectSystem.onPowerUpCollected(event);

    // ASSERT
    // The paddle should now have the power-up component
    assertTrue(paddle.hasComponent(PaddleExpansionComponent.class));

    // The render width should be increased
    double expectedWidth = ORIGINAL_WIDTH * PaddleExpansionComponent.WIDTH_MULTIPLIER;
    assertEquals(expectedWidth, paddleRender.width);

    // The component should store the original width for later
    assertEquals(
        ORIGINAL_WIDTH, paddle.getComponent(PaddleExpansionComponent.class).get().originalWidth);
  }

  @Test
  void update_removesExpiredPowerUpEffect() {
    // ARRANGE: Manually add an almost-expired power-up effect to the paddle
    PaddleExpansionComponent powerUp = new PaddleExpansionComponent(ORIGINAL_WIDTH);
    powerUp.timeRemaining = 0.1; // Almost expired
    paddle.addComponent(powerUp);
    paddleRender.width *= PaddleExpansionComponent.WIDTH_MULTIPLIER; // Manually set expanded width

    // ACT: Update the system with a deltaTime greater than the remaining time
    powerUpEffectSystem.update(List.of(paddle), 0.2);

    // ASSERT
    // The power-up component should have been removed
    assertFalse(paddle.hasComponent(PaddleExpansionComponent.class));

    // The paddle's width should be restored to its original value
    assertEquals(ORIGINAL_WIDTH, paddleRender.width);
  }

  @Test
  void update_doesNotRemoveActivePowerUpEffect() {
    // ARRANGE: Add a fresh power-up effect to the paddle
    PaddleExpansionComponent powerUp = new PaddleExpansionComponent(ORIGINAL_WIDTH);
    paddle.addComponent(powerUp); // Has full duration
    paddleRender.width *= PaddleExpansionComponent.WIDTH_MULTIPLIER;

    // ACT: Update the system with a small deltaTime
    powerUpEffectSystem.update(List.of(paddle), 0.2);

    // ASSERT
    // The component should still be present
    assertTrue(paddle.hasComponent(PaddleExpansionComponent.class));

    // The width should still be expanded
    assertEquals(ORIGINAL_WIDTH * PaddleExpansionComponent.WIDTH_MULTIPLIER, paddleRender.width);

    // The timer should have decreased
    assertTrue(
        paddle.getComponent(PaddleExpansionComponent.class).get().timeRemaining
            < PaddleExpansionComponent.DURATION);
  }
}
