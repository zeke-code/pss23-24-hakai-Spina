package com.zekecode.hakai.events.powerup;

import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.powerups.PowerUpType;

/** An event published when the player's paddle collides with and collects a power-up drop. */
public class PowerUpCollectedEvent {
  public final Entity collectorEntity;
  public final PowerUpType powerUpType;

  public PowerUpCollectedEvent(Entity collectorEntity, PowerUpType powerUpType) {
    this.collectorEntity = collectorEntity;
    this.powerUpType = powerUpType;
  }
}
