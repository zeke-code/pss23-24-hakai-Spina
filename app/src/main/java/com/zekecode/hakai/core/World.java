package com.zekecode.hakai.core;

import com.zekecode.hakai.components.entities.DeadComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * The World class manages entities and systems in the ECS architecture. It provides methods to
 * create entities, add systems, and update the world state.
 */
public class World {
  /** List of all entities in the world. */
  private final List<Entity> entities = new ArrayList<>();

  /** List of entities marked for removal at the end of the update cycle. */
  private final List<Entity> entitiesToRemove = new ArrayList<>();

  /** List of all systems that operate on the entities. */
  private final List<GameSystem> systems = new ArrayList<>();

  /** Counter to assign unique IDs to new entities. */
  private int nextEntityId = 0;

  /** Creates a new entity and adds it to the world. */
  public Entity createEntity() {
    Entity entity = new Entity(nextEntityId++);
    entities.add(entity);
    return entity;
  }

  /** Returns a copy of the list of all entities in the world. */
  public List<Entity> getEntities() {
    return new ArrayList<>(entities);
  }

  /** Marks an entity for removal from the world. */
  public void destroyEntity(Entity entity) {
    if (!entitiesToRemove.contains(entity)) {
      entitiesToRemove.add(entity);
    }
  }

  /** Cleans up entities that are marked for removal. */
  private void cleanupEntities() {
    // Collect entities marked for death
    entities.stream()
        .filter(e -> e.hasComponent(DeadComponent.class))
        .forEach(this::destroyEntity); // Add to the removal queue

    entities.removeAll(entitiesToRemove);
    entitiesToRemove.clear();
  }

  /** Adds a system to the world. */
  public void addSystem(GameSystem system) {
    systems.add(system);
  }

  /** Updates all systems in the world, passing in the delta time since the last update. */
  public void update(double deltaTime) {
    cleanupEntities();
    for (GameSystem system : systems) {
      system.update(new ArrayList<>(entities), deltaTime);
    }
  }
}
