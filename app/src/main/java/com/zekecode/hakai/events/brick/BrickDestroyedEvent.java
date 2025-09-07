package com.zekecode.hakai.events.brick;

import com.zekecode.hakai.core.Entity;

/**
 * An event that is published by the BrickSystem at the moment a brick's HP reaches zero. This event
 * signals that a brick has been officially destroyed.
 */
public class BrickDestroyedEvent {
  public final Entity brickEntity;

  public BrickDestroyedEvent(Entity brickEntity) {
    this.brickEntity = brickEntity;
  }
}
