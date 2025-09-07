package com.zekecode.hakai.events.brick;

import com.zekecode.hakai.core.Entity;

/**
 * An event that is published when a ball collides with a brick.
 *
 * <p>This event carries a reference to the brick entity that was involved in the collision. Systems
 * can listen for this event to handle scoring, entity destruction, sound effects, and other
 * gameplay logic.
 */
public class BrickHitEvent {

  public final Entity brickEntity;

  /**
   * Constructs a new BrickDestroyedEvent.
   *
   * @param brickEntity The brick entity that was hit by the ball.
   */
  public BrickHitEvent(Entity brickEntity) {
    this.brickEntity = brickEntity;
  }
}
