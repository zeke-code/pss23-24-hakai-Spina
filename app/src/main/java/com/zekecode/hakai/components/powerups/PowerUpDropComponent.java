package com.zekecode.hakai.components.powerups;

import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.powerups.PowerUpType;

/**
 * A marker component for the falling power-up item entity. It holds the type of power-up to be
 * activated upon collection.
 */
public class PowerUpDropComponent implements Component {
  public final PowerUpType effectType;

  public PowerUpDropComponent(PowerUpType effectType) {
    this.effectType = effectType;
  }
}
