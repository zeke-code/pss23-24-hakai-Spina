package com.zekecode.hakai.powerups;

import com.zekecode.hakai.core.Entity;

/**
 * The core interface for all power-ups and maluses in the game. Each implementation of this
 * interface will define a specific gameplay effect.
 */
public interface Effect {

  /**
   * Applies the initial effect to a target entity. This is called once when the effect is
   * triggered.
   *
   * @param target The entity to apply the effect to (e.g., the paddle, the ball).
   */
  void apply(Entity target);

  /**
   * Reverts the effect from a target entity. This is called when a timed effect expires to clean up
   * and restore the entity's original state.
   *
   * @param target The entity from which to remove the effect.
   */
  void remove(Entity target);

  /**
   * Called on every frame for the duration of the effect. This is optional and can be used for
   * effects that need continuous updates.
   *
   * @param target The entity being affected.
   * @param deltaTime The time elapsed since the last frame.
   */
  void update(Entity target, double deltaTime);

  /**
   * Returns the duration of the effect in seconds.
   *
   * @return The duration in seconds, or a value <= 0 for instant or permanent effects.
   */
  double getDuration();

  /**
   * Returns the visual category of the effect. This tells the renderer whether to display it as a
   * positive power-up or a negative malus.
   *
   * @return The EffectCategory for this effect.
   */
  EffectCategory getCategory();
}
