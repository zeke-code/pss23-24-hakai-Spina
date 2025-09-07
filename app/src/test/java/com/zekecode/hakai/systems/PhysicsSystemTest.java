package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.BallLostEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicsSystemTest {

  @Mock private EventBus eventBus;

  private PhysicsSystem physicsSystem;
  // TODO: to remove magic numbers when I'm adding the config thingy through yaml (i'm lazy atm
  // sorry)
  private final double SCREEN_WIDTH = 800;
  private final double SCREEN_HEIGHT = 600;

  @BeforeEach
  void setUp() {
    physicsSystem = new PhysicsSystem(SCREEN_WIDTH, SCREEN_HEIGHT, eventBus);
  }

  private Entity createBall(double x, double y, double velX, double velY) {
    Entity ball = new Entity(1);
    ball.addComponent(new BallComponent());
    ball.addComponent(new PositionComponent(x, y));
    ball.addComponent(new VelocityComponent(velX, velY));
    ball.addComponent(new RenderComponent(15, 15, null)); // Size is 15x15
    return ball;
  }

  @Test
  void update_whenBallGoesOffBottomOfScreen_shouldPostBallLostEvent() {
    // ARRANGE
    // Place the ball exactly at the bottom edge, moving down.
    Entity ball = createBall(300, SCREEN_HEIGHT - 15, 0, 100);

    // ACT
    // A small time step will move it past the boundary.
    physicsSystem.update(List.of(ball), 0.016);

    // ASSERT
    // The system should have posted a BallLostEvent.
    ArgumentCaptor<BallLostEvent> eventCaptor = ArgumentCaptor.forClass(BallLostEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // Verify the event contains the specific entity that was lost.
    assertEquals(ball, eventCaptor.getValue().ballEntity);
  }

  @Test
  void update_whenBallIsOnScreen_shouldNotPostBallLostEvent() {
    // ARRANGE
    Entity ball = createBall(300, 300, 50, -50); // In the middle of the screen

    // ACT
    physicsSystem.update(List.of(ball), 0.016);

    // ASSERT
    // No events should be posted.
    verifyNoInteractions(eventBus);
  }

  @Test
  void update_whenBallBouncesOnTopWall_shouldReverseYVelocityAndNotPostEvent() {
    // ARRANGE
    Entity ball = createBall(300, 0, 50, -100); // At the top, moving up
    VelocityComponent velocity = ball.getComponent(VelocityComponent.class).get();

    // ACT
    physicsSystem.update(List.of(ball), 0.016);

    // ASSERT
    // Velocity should be inverted due to the bounce.
    assertEquals(100, velocity.y, "Y velocity should be positive after bouncing off the top wall.");
    // No event should be posted.
    verifyNoInteractions(eventBus);
  }
}
