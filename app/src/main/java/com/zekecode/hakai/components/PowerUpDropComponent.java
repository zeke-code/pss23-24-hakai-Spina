package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/**
 * A marker component for the falling power-up item entity. It holds the type of power-up to be
 * activated upon collection.
 */
public class PowerUpDropComponent implements Component {
  public final String type;

  public PowerUpDropComponent(String type) {
    this.type = type;
  }
}
