package com.zekecode.hakai.events.powerup;

import com.zekecode.hakai.core.Entity;

/** An event published when the player's paddle collides with and collects a power-up drop. */
public class PowerUpCollectedEvent {
  public final Entity collectorEntity;
  public final String powerUpType;

  public PowerUpCollectedEvent(Entity collectorEntity, String powerUpType) {
    this.collectorEntity = collectorEntity;
    this.powerUpType = powerUpType;
  }
}
