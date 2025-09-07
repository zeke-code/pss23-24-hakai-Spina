package com.zekecode.hakai.systems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.entities.DeadComponent;
import com.zekecode.hakai.components.entities.PlayerStateComponent;
import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.LivesChangedEvent;
import com.zekecode.hakai.events.ball.BallLostEvent;
import com.zekecode.hakai.events.ball.ResetBallEvent;
import com.zekecode.hakai.events.states.GameOverEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LivesSystemTest {

  @Mock private World world;
  @Mock private EventBus eventBus;

  private LivesSystem livesSystem;
  private Entity gameStateEntity;
  private PlayerStateComponent playerStateComponent;

  @BeforeEach
  void setUp() {
    livesSystem = new LivesSystem(world, eventBus);
    gameStateEntity = new Entity(0);
    playerStateComponent = new PlayerStateComponent(GameConfig.PLAYER_STARTING_LIVES);
    gameStateEntity.addComponent(playerStateComponent);
  }

  @Test
  void onBallLost_whenItIsTheLastBall_shouldDecrementLifeAndResetBall() {
    // ARRANGE
    Entity ballEntity = new Entity(1);
    ballEntity.addComponent(new BallComponent());
    BallLostEvent event = new BallLostEvent(ballEntity);

    // After the ball is marked as dead, there will be 0 balls left.
    // So we mock the world state to reflect the moment *before* the check.
    when(world.getEntities()).thenReturn(List.of(gameStateEntity, ballEntity));

    // ACT
    livesSystem.onBallLost(event);

    // ASSERT
    // The specific ball that was lost should be marked for deletion.
    assertTrue(ballEntity.hasComponent(DeadComponent.class));
    // Player's lives should be reduced by one.
    assertEquals(2, playerStateComponent.lives);

    // Verify correct events are posted for UI and game logic.
    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(eventBus, times(2)).post(eventCaptor.capture());

    List<Object> postedEvents = eventCaptor.getAllValues();
    assertTrue(
        postedEvents.stream().anyMatch(e -> e instanceof LivesChangedEvent),
        "A LivesChangedEvent should be posted.");
    assertTrue(
        postedEvents.stream().anyMatch(e -> e instanceof ResetBallEvent),
        "A ResetBallEvent should be posted.");
  }

  @Test
  void onBallLost_whenOtherBallsExist_shouldNotDecrementLife() {
    // ARRANGE
    Entity lostBall = new Entity(1);
    lostBall.addComponent(new BallComponent());
    Entity remainingBall = new Entity(2);
    remainingBall.addComponent(new BallComponent());
    BallLostEvent event = new BallLostEvent(lostBall);

    // In this scenario, the world contains two balls.
    when(world.getEntities()).thenReturn(List.of(gameStateEntity, lostBall, remainingBall));

    // ACT
    livesSystem.onBallLost(event);

    // ASSERT
    // The lost ball is marked as dead, but the other remains.
    assertTrue(lostBall.hasComponent(DeadComponent.class));
    assertFalse(remainingBall.hasComponent(DeadComponent.class));

    // CRITICAL: Lives should NOT change.
    assertEquals(3, playerStateComponent.lives);

    // No events related to losing a life should be posted.
    verify(eventBus, never()).post(any(LivesChangedEvent.class));
    verify(eventBus, never()).post(any(ResetBallEvent.class));
    verify(eventBus, never()).post(any(GameOverEvent.class));
  }

  @Test
  void onBallLost_whenLastLifeIsLost_shouldTriggerGameOver() {
    // ARRANGE
    playerStateComponent.lives = 1; // Set player to their last life.
    Entity ballEntity = new Entity(1);
    ballEntity.addComponent(new BallComponent());
    BallLostEvent event = new BallLostEvent(ballEntity);

    when(world.getEntities()).thenReturn(List.of(gameStateEntity, ballEntity));

    // ACT
    livesSystem.onBallLost(event);

    // ASSERT
    assertEquals(0, playerStateComponent.lives);

    // Verify that a GameOverEvent is posted and a ResetBallEvent is NOT.
    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(eventBus, times(2)).post(eventCaptor.capture());

    List<Object> postedEvents = eventCaptor.getAllValues();
    assertTrue(
        postedEvents.stream().anyMatch(e -> e instanceof LivesChangedEvent),
        "A LivesChangedEvent should still be posted.");
    assertTrue(
        postedEvents.stream().anyMatch(e -> e instanceof GameOverEvent),
        "A GameOverEvent should be posted.");
    assertFalse(
        postedEvents.stream().anyMatch(e -> e instanceof ResetBallEvent),
        "A ResetBallEvent should NOT be posted on game over.");
  }
}
