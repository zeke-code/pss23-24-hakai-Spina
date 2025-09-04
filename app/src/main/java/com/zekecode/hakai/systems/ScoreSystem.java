package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.ScoreComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.events.BrickDestroyedEvent;
import com.zekecode.hakai.engine.events.ScoreChangedEvent;
import java.util.List;
import java.util.Optional;

/**
 * Manages the player's score by modifying the global ScoreComponent. This system is now STATELESS.
 */
public class ScoreSystem extends GameSystem {
  private final EventBus eventBus;
  private final World world;

  public ScoreSystem(World world, EventBus eventBus) {
    this.world = world;
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onBrickDestroyed(BrickDestroyedEvent event) {
    // 1. Find the single entity holding the score.
    Optional<Entity> stateEntityOpt = findGameStateEntity();
    if (stateEntityOpt.isEmpty()) return;

    // 2. Get the component and update its value.
    ScoreComponent scoreComp = stateEntityOpt.get().getComponent(ScoreComponent.class).get();
    scoreComp.score += 10; // Increase score on the component

    System.out.println("SCORE: " + scoreComp.score);
    eventBus.post(new ScoreChangedEvent(scoreComp.score));
  }

  private Optional<Entity> findGameStateEntity() {
    for (Entity entity : world.getEntities()) {
      if (entity.hasComponent(ScoreComponent.class)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {}
}
