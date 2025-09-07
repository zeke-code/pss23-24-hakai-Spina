package com.zekecode.hakai.components.entities;

import com.zekecode.hakai.core.Component;

/** A component that tracks the player's state, including lives and score. */
public class PlayerStateComponent implements Component {
  public int lives;

  public PlayerStateComponent(int startingLives) {
    this.lives = startingLives;
  }
}
