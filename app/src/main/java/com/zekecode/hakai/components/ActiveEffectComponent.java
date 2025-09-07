package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;
import com.zekecode.hakai.powerups.Effect;

/**
 * A component attached to an entity that is currently under the influence of a timed power-up or
 * malus. It holds a reference to the effect's logic and tracks its remaining duration.
 */
public class ActiveEffectComponent implements Component {
  public final Effect effect;
  public double timeRemaining;

  public ActiveEffectComponent(Effect effect) {
    this.effect = effect;
    this.timeRemaining = effect.getDuration();
  }
}
