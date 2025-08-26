package com.zekecode.hakai.ecs;

import java.util.List;

/**
 * Abstract class representing a system in the ECS architecture. Systems contain the logic to
 * process entities with specific components.
 */
public abstract class GameSystem {
  public abstract void update(List<Entity> entities, double deltaTime);
}
