package com.zekecode.hakai.engine.events;

import com.zekecode.hakai.core.Entity;

/**
 * An event that is published when a ball goes off the bottom of the screen. It now carries a
 * reference to the specific ball entity that was lost.
 */
public class BallLostEvent {
  public final Entity ballEntity;

  public BallLostEvent(Entity ballEntity) {
    this.ballEntity = ballEntity;
  }
}
