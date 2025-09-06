package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.input.InputManager;
import java.util.List;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovementSystemTest {

  @Mock private InputManager inputManager;

  private MovementSystem movementSystem;
  private Entity player;
  private VelocityComponent playerVelocity;
  private static final double PADDLE_SPEED = 400.0;

  @BeforeEach
  void setUp() {
    movementSystem = new MovementSystem(inputManager);

    player = new Entity(1);
    playerVelocity = new VelocityComponent(0, 0);
    player.addComponent(new InputComponent());
    player.addComponent(playerVelocity);

    lenient().when(inputManager.isKeyPressed(KeyCode.LEFT)).thenReturn(false);
    lenient().when(inputManager.isKeyPressed(KeyCode.A)).thenReturn(false);
    lenient().when(inputManager.isKeyPressed(KeyCode.RIGHT)).thenReturn(false);
    lenient().when(inputManager.isKeyPressed(KeyCode.D)).thenReturn(false);
  }

  @Test
  void update_whenLeftKeyIsPressed_setsNegativeHorizontalVelocity() {
    // ARRANGE: Override only the key we care about for this test.
    when(inputManager.isKeyPressed(KeyCode.LEFT)).thenReturn(true);

    // ACT
    movementSystem.update(List.of(player), 0.016);

    // ASSERT
    assertEquals(-PADDLE_SPEED, playerVelocity.x, "Velocity should be negative for left movement.");
  }

  @Test
  void update_whenAlternateLeftKeyIsPressed_setsNegativeHorizontalVelocity() {
    // ARRANGE
    when(inputManager.isKeyPressed(KeyCode.A)).thenReturn(true);

    // ACT
    movementSystem.update(List.of(player), 0.016);

    // ASSERT
    assertEquals(-PADDLE_SPEED, playerVelocity.x, "Velocity should be negative for 'A' key.");
  }

  @Test
  void update_whenRightKeyIsPressed_setsPositiveHorizontalVelocity() {
    // ARRANGE
    when(inputManager.isKeyPressed(KeyCode.RIGHT)).thenReturn(true);

    // ACT
    movementSystem.update(List.of(player), 0.016);

    // ASSERT
    assertEquals(PADDLE_SPEED, playerVelocity.x, "Velocity should be positive for right movement.");
  }

  @Test
  void update_whenNoKeysArePressed_setsZeroHorizontalVelocity() {
    // ARRANGE: Ensure player has some leftover velocity.
    playerVelocity.x = PADDLE_SPEED;

    // ACT
    movementSystem.update(List.of(player), 0.016);

    // ASSERT
    assertEquals(0, playerVelocity.x, "Velocity should be zero when no movement keys are pressed.");
  }
}
