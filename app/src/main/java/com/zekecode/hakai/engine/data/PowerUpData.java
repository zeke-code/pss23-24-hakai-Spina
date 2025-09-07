package com.zekecode.hakai.engine.data;

import com.zekecode.hakai.powerups.PowerUpTrigger;

/**
 * A data class representing the power-up configuration for a brick type, loaded from a level's YAML
 * file.
 */
public class PowerUpData {
  public String type; // The unique identifier string for the effect (e.g., "PADDLE_EXPAND")
  public PowerUpTrigger trigger; // How the effect is activated (INSTANT or ON_COLLECT)
}
