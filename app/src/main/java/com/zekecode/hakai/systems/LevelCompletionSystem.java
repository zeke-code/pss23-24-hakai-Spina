package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.LevelClearEvent;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import java.util.List;

/**
 * A system that checks for the level completion condition (i.e., all bricks destroyed). It listens
 * for brick destruction events and, if no bricks remain, it fires a LevelClearEvent.
 */
public class LevelCompletionSystem extends GameSystem {

  private final World world;
  private final EventBus eventBus;

  public LevelCompletionSystem(World world, EventBus eventBus) {
    this.world = world;
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onBrickDestroyed(BrickDestroyedEvent event) {
    // After a brick is destroyed, check if any more bricks are left in the world.
    long remainingBricks =
        world.getEntities().stream().filter(e -> e.hasComponent(BrickComponent.class)).count();

    // The count will be 1 right after destruction, as the dead brick hasn't been removed yet.
    // So, we check if the count is <= 1.
    if (remainingBricks <= 1) {
      System.out.println("Last brick destroyed! Level clear!");
      eventBus.post(new LevelClearEvent());
    }
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven.
  }
}
