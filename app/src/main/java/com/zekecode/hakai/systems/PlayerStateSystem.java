package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.PlayerStateComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.events.BallLostEvent;
import com.zekecode.hakai.engine.events.GameOverEvent;
import com.zekecode.hakai.engine.events.LivesChangedEvent;
import com.zekecode.hakai.engine.events.ResetBallEvent;
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

  /** This method listens for the BallLostEvent. */
  @Subscribe
  public void onBallLost(BallLostEvent event) {
    // 1. Find the single entity that holds the player's state.
    Optional<Entity> stateEntityOpt = findGameStateEntity();

    if (stateEntityOpt.isEmpty()) {
      System.err.println("PlayerStateSystem could not find the game state entity!");
      return;
    }

    // 2. Get the component and modify its data.
    PlayerStateComponent state =
        stateEntityOpt.get().getComponent(PlayerStateComponent.class).get();
    state.lives--; // Decrement the lives on the component

    System.out.println("LIFE LOST! Lives remaining: " + state.lives);
    eventBus.post(new LivesChangedEvent(state.lives));

    if (state.lives <= 0) {
      eventBus.post(new GameOverEvent());
    } else {
      eventBus.post(new ResetBallEvent());
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
