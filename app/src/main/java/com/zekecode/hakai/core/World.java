package com.zekecode.hakai.core;

import com.zekecode.hakai.components.entities.DeadComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * The World class manages entities and systems in the ECS architecture. It provides methods to
 * create entities, add systems, and update the world state.
 */
public class World {
  private final List<Entity> entities = new ArrayList<>();
  private final List<Entity> entitiesToRemove = new ArrayList<>();
  private final List<GameSystem> systems = new ArrayList<>();
  private int nextEntityId = 0;

  public Entity createEntity() {
    Entity entity = new Entity(nextEntityId++);
    entities.add(entity);
    return entity;
  }

  public List<Entity> getEntities() {
    return new ArrayList<>(entities);
  }

  public void destroyEntity(Entity entity) {
    if (!entitiesToRemove.contains(entity)) {
      entitiesToRemove.add(entity);
    }
  }

  private void cleanupEntities() {
    // Collect entities marked for death
    entities.stream()
        .filter(e -> e.hasComponent(DeadComponent.class))
        .forEach(this::destroyEntity); // Add to the removal queue

    entities.removeAll(entitiesToRemove);
    entitiesToRemove.clear();
  }

  public void addSystem(GameSystem system) {
    systems.add(system);
  }

  public void update(double deltaTime) {
    cleanupEntities();
    for (GameSystem system : systems) {
      system.update(new ArrayList<>(entities), deltaTime);
    }
  }
}
