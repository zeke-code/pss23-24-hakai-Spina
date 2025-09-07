package com.zekecode.hakai.components.physics;

import com.zekecode.hakai.core.Component;

/** A component that represents the position of an entity in 2D space. */
public class PositionComponent implements Component {
  public double x;
  public double y;

  public PositionComponent(double x, double y) {
    this.x = x;
    this.y = y;
  }
}
