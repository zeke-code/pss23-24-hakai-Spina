package com.zekecode.hakai.systems.powerups;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.PowerUpComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.engine.events.BrickDestroyedEvent;
import com.zekecode.hakai.entities.EntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PowerUpSpawnSystemTest {

  @Mock private EntityFactory entityFactory;

  private PowerUpSpawnSystem spawnSystem;

  @BeforeEach
  void setUp() {
    spawnSystem = new PowerUpSpawnSystem(entityFactory);
  }

  @Test
  void onBrickDestroyed_whenBrickHasPowerUp_shouldCreatePowerUpDrop() {
    // ARRANGE
    // Create a brick entity that has a position and a power-up.
    Entity brickWithPowerUp = new Entity(1);
    brickWithPowerUp.addComponent(new PositionComponent(150, 80));
    brickWithPowerUp.addComponent(new PowerUpComponent("PADDLE_SIZE_INCREASE"));
    BrickDestroyedEvent event = new BrickDestroyedEvent(brickWithPowerUp);

    // ACT
    spawnSystem.onBrickDestroyed(event);

    // ASSERT
    // Verify the entity factory was called to create the drop.
    ArgumentCaptor<Double> xCaptor = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<Double> yCaptor = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);

    verify(entityFactory, times(1))
        .createPowerUpDrop(xCaptor.capture(), yCaptor.capture(), typeCaptor.capture());

    // Check that the power-up was created at the brick's location.
    assertEquals(150, xCaptor.getValue());
    assertEquals(80, yCaptor.getValue());
    // Check that the correct type of power-up was created.
    assertEquals("PADDLE_SIZE_INCREASE", typeCaptor.getValue());
  }

  @Test
  void onBrickDestroyed_whenBrickHasNoPowerUp_shouldDoNothing() {
    // ARRANGE
    // Create a brick entity with only a position (no PowerUpComponent).
    Entity normalBrick = new Entity(2);
    normalBrick.addComponent(new PositionComponent(200, 100));
    BrickDestroyedEvent event = new BrickDestroyedEvent(normalBrick);

    // ACT
    spawnSystem.onBrickDestroyed(event);

    // ASSERT
    // The factory should never be called.
    verifyNoInteractions(entityFactory);
  }
}
