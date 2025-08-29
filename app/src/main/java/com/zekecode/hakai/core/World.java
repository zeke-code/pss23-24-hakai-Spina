package com.zekecode.hakai.core;

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

  public void destroyEntity(Entity entity) {
    if (!entitiesToRemove.contains(entity)) {
      entitiesToRemove.add(entity);
    }
  }

  private void cleanupEntities() {
    entities.removeAll(entitiesToRemove);
    entitiesToRemove.clear();
  }

  public void addSystem(GameSystem system) {
    systems.add(system);
  }

  public void update(double deltaTime) {
    for (GameSystem system : systems) {
      system.update(entities, deltaTime);
    }
    cleanupEntities();
  }
}
