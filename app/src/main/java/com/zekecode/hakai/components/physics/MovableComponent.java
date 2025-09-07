package com.zekecode.hakai.components.physics;

import com.zekecode.hakai.core.Component;

/** A component that defines an entity's movement speed. */
public class MovableComponent implements Component {
  public double speed;

  public MovableComponent(double speed) {
    this.speed = speed;
  }
}
