package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.engine.data.PowerUpData;

/**
 * A component that marks a brick as containing a power-up. It holds the configuration data for the
 * effect, including its type and trigger condition.
 */
public class PowerUpComponent implements Component {
  public final PowerUpData powerUpData;

  public PowerUpComponent(PowerUpData powerUpData) {
    this.powerUpData = powerUpData;
  }
}
