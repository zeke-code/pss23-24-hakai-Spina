package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.ResetBallEvent;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.entities.EntityFactory;
import java.util.List;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallSystemTest {

  @Mock private InputManager inputManager;
  @Mock private EntityFactory entityFactory;

  private BallSystem ballSystem;
  private Entity paddle;
  private Entity ball;

  @BeforeEach
  void setUp() {
    ballSystem = new BallSystem(inputManager, entityFactory);

    // Create a paddle for the ball to stick to
    paddle = new Entity(1);
    paddle.addComponent(new InputComponent()); // Identifies it as the paddle
    paddle.addComponent(new PositionComponent(350, 550)); // Center of 800x600 screen
    paddle.addComponent(new RenderComponent(100, 20, null));

    // Create a ball that should be stuck to the paddle
    ball = new Entity(2);
    ball.addComponent(new BallComponent());
    ball.addComponent(new BallStuckToPaddleComponent());
    ball.addComponent(new PositionComponent(0, 0)); // Start at a dummy position
    ball.addComponent(new VelocityComponent(0, 0));
    ball.addComponent(new RenderComponent(15, 15, null));
  }

  @Test
  void onResetBall_shouldCreateNewBall() {
    // ARRANGE
    ResetBallEvent event = new ResetBallEvent();

    // ACT
    ballSystem.onResetBall(event);

    // ASSERT
    // Verify that the system delegates ball creation to the factory.
    verify(entityFactory, times(1)).createBall(anyDouble(), anyDouble());
  }

  @Test
  void update_whenBallIsStuck_shouldCenterBallOnPaddle() {
    // ARRANGE
    // Paddle is at x:350, width:100. Center is 400.
    // Ball is at x:0, width:15. Center is 7.5.
    // Expected ball position x = 400 - 7.5 = 392.5
    // Expected ball position y = 550 (paddle y) - 15 (ball height) = 535

    // ACT
    ballSystem.update(List.of(paddle, ball), 0.016);

    // ASSERT
    PositionComponent ballPos = ball.getComponent(PositionComponent.class).get();
    assertEquals(392.5, ballPos.x, "Ball X position should be centered on the paddle.");
    assertEquals(535, ballPos.y, "Ball Y position should be directly on top of the paddle.");
  }

  @Test
  void update_whenLaunchKeyIsPressed_shouldReleaseBallAndSetVelocity() {
    // ARRANGE
    // Simulate the player pressing the 'W' key to launch.
    when(inputManager.isKeyPressed(KeyCode.W)).thenReturn(true);

    // ACT
    ballSystem.update(List.of(paddle, ball), 0.016);

    // ASSERT
    // The "stuck" component should be removed, releasing the ball.
    assertFalse(
        ball.hasComponent(BallStuckToPaddleComponent.class),
        "BallStuckToPaddleComponent should be removed on launch.");

    // The ball should be given an initial upward velocity.
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    assertTrue(ballVelocity.y < 0, "Ball Y velocity should be negative (upwards) on launch.");
    // Check for the specific launch velocity set in the system.
    assertEquals(-350, ballVelocity.y);
  }

  @Test
  void update_whenLaunchKeyIsNotPressed_shouldKeepBallStuck() {
    // ARRANGE
    // Ensure no keys are pressed.
    when(inputManager.isKeyPressed(any(KeyCode.class))).thenReturn(false);

    // ACT
    ballSystem.update(List.of(paddle, ball), 0.016);

    // ASSERT
    // The ball should remain in its "stuck" state.
    assertTrue(
        ball.hasComponent(BallStuckToPaddleComponent.class),
        "Ball should remain stuck if launch key is not pressed.");

    // Its velocity should remain zero.
    VelocityComponent ballVelocity = ball.getComponent(VelocityComponent.class).get();
    assertEquals(0, ballVelocity.y);
    assertEquals(0, ballVelocity.x);
  }
}
