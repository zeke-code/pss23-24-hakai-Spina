package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.entities.DeadComponent;
import com.zekecode.hakai.components.entities.PlayerStateComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.GameOverEvent;
import com.zekecode.hakai.events.LivesChangedEvent;
import com.zekecode.hakai.events.ball.BallLostEvent;
import com.zekecode.hakai.events.ball.ResetBallEvent;
import java.util.List;
import java.util.Optional;

/** Manages the player's state by modifying the global PlayerStateComponent. */
public class PlayerStateSystem extends GameSystem {

  private final EventBus eventBus;
  private final World world;

  public PlayerStateSystem(World world, EventBus eventBus) {
    this.world = world;
    this.eventBus = eventBus;
  }

  /** This method listens for the BallLostEvent. It now implements the multi-ball logic. */
  @Subscribe
  public void onBallLost(BallLostEvent event) {
    // 1. Mark the specific ball that was lost as "dead".
    // It will be removed from the world on the next update cycle.
    event.ballEntity.addComponent(new DeadComponent());

    // 2. Count how many balls are *still active* in the game world.
    long remainingBalls =
        world.getEntities().stream()
            .filter(
                e -> e.hasComponent(BallComponent.class) && !e.hasComponent(DeadComponent.class))
            .count();

    // 3. Only if there are no balls left, do we process losing a life.
    if (remainingBalls == 0) {
      System.out.println("LAST BALL LOST! Processing life lost.");

      // Find the single entity that holds the player's state.
      Optional<Entity> stateEntityOpt = findGameStateEntity();
      if (stateEntityOpt.isEmpty()) {
        System.err.println("PlayerStateSystem could not find the game state entity!");
        return;
      }

      // Get the component and modify its data.
      PlayerStateComponent state =
          stateEntityOpt.get().getComponent(PlayerStateComponent.class).get();
      state.lives--; // Decrement the lives on the component

      System.out.println("Lives remaining: " + state.lives);
      eventBus.post(new LivesChangedEvent(state.lives));

      if (state.lives <= 0) {
        eventBus.post(new GameOverEvent());
      } else {
        // Post an event to reset ONE ball back on the paddle.
        eventBus.post(new ResetBallEvent());
      }
    }
  }

  // Helper method to query the world.
  private Optional<Entity> findGameStateEntity() {
    for (Entity entity : world.getEntities()) {
      if (entity.hasComponent(PlayerStateComponent.class)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system remains purely event-driven.
  }
}
