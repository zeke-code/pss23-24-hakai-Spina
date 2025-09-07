package com.zekecode.hakai.powerups;

/** Defines the condition under which a power-up or malus effect is activated. */
public enum PowerUpTrigger {
  /** The effect is applied instantly when the containing brick is destroyed. */
  INSTANT,

  /** The effect is spawned as a collectible item that must be picked up by the player. */
  ON_COLLECT
}
