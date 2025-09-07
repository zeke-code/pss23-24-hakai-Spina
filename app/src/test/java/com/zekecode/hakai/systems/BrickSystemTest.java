package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.components.entities.DeadComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import com.zekecode.hakai.events.brick.BrickHitEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrickSystemTest {

  @Mock private EventBus eventBus;

  private BrickSystem brickSystem;

  @BeforeEach
  void setUp() {
    brickSystem = new BrickSystem(eventBus);
  }

  @Test
  void onBrickHit_withMultipleHP_shouldDecrementHPButNotDestroy() {
    // ARRANGE
    Entity brick = new Entity(1);
    BrickComponent brickComp = new BrickComponent(2);
    brick.addComponent(brickComp);
    BrickHitEvent event = new BrickHitEvent(brick);

    // ACT
    brickSystem.onBrickHit(event);

    // ASSERT
    assertEquals(1, brickComp.hp, "HP should decrement by 1.");
    assertFalse(brick.hasComponent(DeadComponent.class), "Brick should not be marked dead.");
    verifyNoInteractions(eventBus); // No destruction event should be posted
  }

  @Test
  void onBrickHit_withOneHP_shouldDecrementHPAndDestroyBrick() {
    // ARRANGE
    Entity brick = new Entity(1);
    BrickComponent brickComp = new BrickComponent(1);
    brick.addComponent(brickComp);
    BrickHitEvent event = new BrickHitEvent(brick);

    // ACT
    brickSystem.onBrickHit(event);

    // ASSERT
    assertEquals(0, brickComp.hp, "HP should reach 0.");
    assertTrue(brick.hasComponent(DeadComponent.class), "Brick should be marked dead.");

    // Verify the destruction event was posted
    ArgumentCaptor<BrickDestroyedEvent> eventCaptor =
        ArgumentCaptor.forClass(BrickDestroyedEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // Verify the event carries a reference to the destroyed brick
    assertEquals(brick, eventCaptor.getValue().brickEntity);
  }
}
