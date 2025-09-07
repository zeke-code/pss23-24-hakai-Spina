package com.zekecode.hakai.components.powerups;

import com.zekecode.hakai.core.Component;

/**
 * A marker component for the falling power-up item entity. It holds the type of power-up to be
 * activated upon collection.
 */
public class PowerUpDropComponent implements Component {
  public final String effectType;

  public PowerUpDropComponent(String effectType) {
    this.effectType = effectType;
  }
}
