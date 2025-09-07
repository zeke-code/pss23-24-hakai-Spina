package com.zekecode.hakai.components.entities;

import com.zekecode.hakai.core.Component;

/** A component that represents a brick in the game, with a certain strength (hit points). */
public class BrickComponent implements Component {
  public int hp; // Hit points / strength of the brick

  public BrickComponent(int hp) {
    this.hp = hp;
  }
}
