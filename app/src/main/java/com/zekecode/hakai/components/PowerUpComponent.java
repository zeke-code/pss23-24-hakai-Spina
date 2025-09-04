package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/** A component that marks a brick as containing a power-up to be spawned on destruction. */
public class PowerUpComponent implements Component {
  public final String type;

  public PowerUpComponent(String type) {
    this.type = type;
  }
}
