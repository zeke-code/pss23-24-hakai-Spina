// ===== File: .\app\src\test\java\com\zekecode\hakai\systems\powerups\PowerUpSystemTest.java =====
package com.zekecode.hakai.systems.powerups;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.paddle.PaddleStateComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.powerups.ActiveEffectsComponent;
import com.zekecode.hakai.components.powerups.PowerUpComponent;
import com.zekecode.hakai.config.data.PowerUpData;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import com.zekecode.hakai.events.powerup.PowerUpCollectedEvent;
import com.zekecode.hakai.powerups.EffectCategory;
import com.zekecode.hakai.powerups.EffectRegistry;
import com.zekecode.hakai.powerups.PowerUpTrigger;
import com.zekecode.hakai.powerups.PowerUpType;
import com.zekecode.hakai.powerups.effects.PaddleExpandEffect;
import com.zekecode.hakai.powerups.effects.PaddleSlowEffect;
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
    effectManagementSystem = new EffectManagementSystem(effectRegistry);
  }

  @Test
  void onBrickDestroyed_withOnCollectTrigger_shouldSpawnPowerUpDrop() {
    // ARRANGE
    PowerUpData powerUpData = new PowerUpData();
    powerUpData.type = PowerUpType.PADDLE_EXPAND;
    powerUpData.trigger = PowerUpTrigger.ON_COLLECT;

    Entity brick = new Entity(1);
    brick.addComponent(new PowerUpComponent(powerUpData));
    brick.addComponent(new PositionComponent(100, 50));
    BrickDestroyedEvent event = new BrickDestroyedEvent(brick);

    when(effectRegistry.getEffect(PowerUpType.PADDLE_EXPAND))
        .thenReturn(Optional.of(new PaddleExpandEffect()));

    // ACT
    powerUpSystem.onBrickDestroyed(event);

    // ASSERT
    // Verify it was called with the correct category.
    verify(entityFactory, times(1))
        .createPowerUpDrop(100, 50, PowerUpType.PADDLE_EXPAND, EffectCategory.POSITIVE);
  }

  @Test
  void onPowerUpCollected_shouldApplyEffectAndAddComponent() {
    // ARRANGE
    Entity paddle = new Entity(1);
    paddle.addComponent(new RenderComponent(100, 20, null));
    PowerUpCollectedEvent event = new PowerUpCollectedEvent(paddle, PowerUpType.PADDLE_EXPAND);
    PaddleExpandEffect effect = new PaddleExpandEffect();

    when(effectRegistry.getEffect(PowerUpType.PADDLE_EXPAND)).thenReturn(Optional.of(effect));

    // ACT
    powerUpSystem.onPowerUpCollected(event);

    // ASSERT
    assertTrue(
        paddle.hasComponent(ActiveEffectsComponent.class),
        "ActiveEffectsComponent should be added.");
    assertTrue(
        paddle
            .getComponent(ActiveEffectsComponent.class)
            .get()
            .activeEffects
            .containsKey(PowerUpType.PADDLE_EXPAND),
        "The map should contain the PADDLE_EXPAND effect.");
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
    paddle.addComponent(new PaddleStateComponent(100)); // State from effect.apply()

    PaddleExpandEffect effect = new PaddleExpandEffect();
    ActiveEffectsComponent effectsComp = new ActiveEffectsComponent();
    // Simulate an almost-expired effect
    effectsComp.activeEffects.put(PowerUpType.PADDLE_EXPAND, 0.1);
    paddle.addComponent(effectsComp);

    // The management system will need to look up the effect to call its remove() method.
    when(effectRegistry.getEffect(PowerUpType.PADDLE_EXPAND)).thenReturn(Optional.of(effect));

    // ACT
    // Update with a delta time greater than the remaining time to force expiration.
    effectManagementSystem.update(List.of(paddle), 0.2);

    // ASSERT
    assertFalse(
        paddle.hasComponent(ActiveEffectsComponent.class),
        "ActiveEffectsComponent should be removed once its map is empty.");
    assertFalse(
        paddle.hasComponent(PaddleStateComponent.class),
        "PaddleStateComponent should be removed by the effect's remove() method.");
    assertEquals(
        100,
        paddle.getComponent(RenderComponent.class).get().width,
        "Paddle width should be restored to original.");
  }

  @Test
  void effectManagementSystem_update_shouldHandleMultipleConcurrentEffects() {
    // ARRANGE: An entity with two active effects, one about to expire.
    Entity paddle = new Entity(1);
    ActiveEffectsComponent effectsComp = new ActiveEffectsComponent();
    effectsComp.activeEffects.put(PowerUpType.PADDLE_EXPAND, 0.1); // Will expire
    effectsComp.activeEffects.put(PowerUpType.PADDLE_SLOW, 10.0); // Will not expire
    paddle.addComponent(effectsComp);

    PaddleExpandEffect expandEffect = spy(new PaddleExpandEffect());
    PaddleSlowEffect slowEffect = spy(new PaddleSlowEffect());

    when(effectRegistry.getEffect(PowerUpType.PADDLE_EXPAND)).thenReturn(Optional.of(expandEffect));
    when(effectRegistry.getEffect(PowerUpType.PADDLE_SLOW)).thenReturn(Optional.of(slowEffect));

    // ACT
    effectManagementSystem.update(List.of(paddle), 0.2);

    // ASSERT
    // Verify the remove method was called ONLY for the expired effect.
    verify(expandEffect, times(1)).remove(paddle);
    verify(slowEffect, never()).remove(paddle);

    // The main component should still exist, but only contain the non-expired effect.
    assertTrue(paddle.hasComponent(ActiveEffectsComponent.class));
    ActiveEffectsComponent remainingEffects =
        paddle.getComponent(ActiveEffectsComponent.class).get();
    assertEquals(1, remainingEffects.activeEffects.size());
    assertTrue(remainingEffects.activeEffects.containsKey(PowerUpType.PADDLE_SLOW));
    assertFalse(remainingEffects.activeEffects.containsKey(PowerUpType.PADDLE_EXPAND));
  }
}
