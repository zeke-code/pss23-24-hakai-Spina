package com.zekecode.hakai.powerups;

/**
 * Defines the complete set of unique identifiers for all power-up and malus effects in the game.
 * Using an enum provides compile-time safety and prevents errors from typos in configuration files.
 */
public enum PowerUpType {
  // Positive Effects
  PADDLE_EXPAND,
  SPAWN_BALL,

  // Maluses
  PADDLE_SLOW
}
