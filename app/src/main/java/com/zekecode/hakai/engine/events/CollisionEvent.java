package com.zekecode.hakai.engine.events;

import com.zekecode.hakai.core.Entity;

/**
 * A generic event published by the CollisionSystem when any two entities' bounding boxes overlap.
 *
 * <p>Specialized systems will listen for this event and decide how to handle the interaction based
 * on the components of the entities involved.
 */
public class CollisionEvent {
  public final Entity entityA;
  public final Entity entityB;

  public CollisionEvent(Entity entityA, Entity entityB) {
    this.entityA = entityA;
    this.entityB = entityB;
  }
}
