package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.entities.ScoreComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.ScoreChangedEvent;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScoreSystemTest {

  @Mock private World world;
  @Mock private EventBus eventBus;

  private ScoreSystem scoreSystem;

  @BeforeEach
  void setUp() {
    // This runs before each test, ensuring a clean state
    scoreSystem = new ScoreSystem(world, eventBus);
  }

  @Test
  void onBrickDestroyed_shouldIncreaseScoreAndPostEvent() {
    // --- 1. ARRANGE ---
    // Create a fake entity to represent the game state
    Entity gameStateEntity = new Entity(1);
    ScoreComponent scoreComponent = new ScoreComponent(); // Starts at 0
    scoreComponent.score = 50; // Start with a non-zero score
    gameStateEntity.addComponent(scoreComponent);

    // Create a fake brick entity that was "destroyed"
    Entity brickEntity = new Entity(2);
    BrickDestroyedEvent event = new BrickDestroyedEvent(brickEntity);

    // Tell our mock World to return the fake game state entity when asked.
    when(world.getEntities()).thenReturn(List.of(gameStateEntity));

    // --- 2. ACT ---
    // Manually call the event handler method on our system instance
    scoreSystem.onBrickDestroyed(event);

    // --- 3. ASSERT ---
    // Verify that the score on our component was increased by 10
    assertEquals(60, scoreComponent.score, "Score should increase by 10.");

    // Use an ArgumentCaptor to "capture" the event that was posted to the bus
    ArgumentCaptor<ScoreChangedEvent> eventCaptor =
        ArgumentCaptor.forClass(ScoreChangedEvent.class);

    // Verify that eventBus.post() was called exactly once with a ScoreChangedEvent
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // Check that the captured event contains the correct new score
    assertEquals(
        60, eventCaptor.getValue().newScore, "ScoreChangedEvent should contain the new score.");
  }

  @Test
  void onBrickDestroyed_shouldDoNothingIfNoScoreEntityExists() {
    // --- ARRANGE ---
    // Simulate a world with no game state entity
    when(world.getEntities()).thenReturn(List.of());
    BrickDestroyedEvent event = new BrickDestroyedEvent(new Entity(1));

    // --- ACT ---
    scoreSystem.onBrickDestroyed(event);

    // --- ASSERT ---
    // Verify that the event bus was never touched, preventing null pointer exceptions.
    verifyNoInteractions(eventBus);
  }
}
