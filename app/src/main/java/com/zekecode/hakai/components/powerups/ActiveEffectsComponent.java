package com.zekecode.hakai.components.powerups;

import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.powerups.PowerUpType;
import java.util.HashMap;
import java.util.Map;

/**
 * A component that holds a collection of all active, timed effects on an entity. This replaces the
 * single-instance ActiveEffectComponent to allow for multiple concurrent power-ups and maluses.
 */
public class ActiveEffectsComponent implements Component {
  // Map key is the unique PowerUpType enum, value is the time remaining.
  public final Map<PowerUpType, Double> activeEffects = new HashMap<>();
}
