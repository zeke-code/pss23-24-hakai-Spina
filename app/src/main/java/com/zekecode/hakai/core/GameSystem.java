package com.zekecode.hakai.core;

import java.util.List;

/**
 * Abstract class representing a system in the ECS architecture. Systems contain the logic to
 * process entities with specific components.
 */
public abstract class GameSystem {
  /**
   * Updates the system by processing the given list of entities.
   *
   * @param entities The list of entities to process.
   * @param deltaTime The time elapsed since the last update, in seconds.
   */
  public abstract void update(List<Entity> entities, double deltaTime);
}
