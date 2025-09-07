package com.zekecode.hakai.systems.powerups;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.paddle.PaddleStateComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.powerups.ActiveEffectComponent;
import com.zekecode.hakai.components.powerups.PowerUpComponent;
import com.zekecode.hakai.config.data.PowerUpData;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import com.zekecode.hakai.events.powerup.PowerUpCollectedEvent;
import com.zekecode.hakai.powerups.EffectRegistry;
import com.zekecode.hakai.powerups.PowerUpTrigger;
import com.zekecode.hakai.powerups.effects.PaddleExpandEffect;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PowerUpSystemTest {

  @Mock private World world;
  @Mock private EntityFactory entityFactory;
  @Mock private EffectRegistry effectRegistry;

  private PowerUpSystem powerUpSystem;
  private EffectManagementSystem effectManagementSystem;

  @BeforeEach
  void setUp() {
    powerUpSystem = new PowerUpSystem(world, entityFactory, effectRegistry);
    effectManagementSystem = new EffectManagementSystem();
  }

  @Test
  void onBrickDestroyed_withOnCollectTrigger_shouldSpawnPowerUpDrop() {
    // ARRANGE
    PowerUpData powerUpData = new PowerUpData();
    powerUpData.type = "PADDLE_EXPAND";
    powerUpData.trigger = PowerUpTrigger.ON_COLLECT;

    Entity brick = new Entity(1);
    brick.addComponent(new PowerUpComponent(powerUpData));
    brick.addComponent(new PositionComponent(100, 50));
    BrickDestroyedEvent event = new BrickDestroyedEvent(brick);

    // ACT
    powerUpSystem.onBrickDestroyed(event);

    // ASSERT
    verify(entityFactory, times(1)).createPowerUpDrop(100, 50, "PADDLE_EXPAND");
  }

  @Test
  void onPowerUpCollected_shouldApplyEffectAndAddComponent() {
    // ARRANGE
    Entity paddle = new Entity(1);
    paddle.addComponent(new RenderComponent(100, 20, null));
    PowerUpCollectedEvent event = new PowerUpCollectedEvent(paddle, "PADDLE_EXPAND");
    PaddleExpandEffect effect = new PaddleExpandEffect();

    when(effectRegistry.getEffect("PADDLE_EXPAND")).thenReturn(Optional.of(effect));

    // ACT
    powerUpSystem.onPowerUpCollected(event);

    // ASSERT
    assertTrue(
        paddle.hasComponent(ActiveEffectComponent.class), "ActiveEffectComponent should be added.");
    assertTrue(
        paddle.hasComponent(PaddleStateComponent.class),
        "PaddleStateComponent should be added to store original width.");
    assertEquals(
        150,
        paddle.getComponent(RenderComponent.class).get().width,
        "Paddle width should be increased.");
  }

  @Test
  void effectManagementSystem_update_shouldRemoveExpiredEffect() {
    // ARRANGE
    Entity paddle = new Entity(1);
    paddle.addComponent(new RenderComponent(150, 20, null));
    paddle.addComponent(new PaddleStateComponent(100)); // Original width was 100

    PaddleExpandEffect effect = new PaddleExpandEffect();
    ActiveEffectComponent activeEffect = new ActiveEffectComponent(effect);
    activeEffect.timeRemaining = 0.1; // Almost expired
    paddle.addComponent(activeEffect);

    // ACT
    // Update with a delta time greater than the remaining time
    effectManagementSystem.update(List.of(paddle), 0.2);

    // ASSERT
    assertFalse(
        paddle.hasComponent(ActiveEffectComponent.class),
        "ActiveEffectComponent should be removed.");
    assertFalse(
        paddle.hasComponent(PaddleStateComponent.class),
        "PaddleStateComponent should be removed on expiration.");
    assertEquals(
        100,
        paddle.getComponent(RenderComponent.class).get().width,
        "Paddle width should be restored.");
  }
}
