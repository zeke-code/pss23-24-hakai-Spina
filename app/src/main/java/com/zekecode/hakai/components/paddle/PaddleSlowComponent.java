package com.zekecode.hakai.components.paddle;

import com.zekecode.hakai.core.Component;

/** A state component that marks the paddle as slowed and stores its original speed. */
public class PaddleSlowComponent implements Component {
  public final double originalSpeed;

  public PaddleSlowComponent(double originalSpeed) {
    this.originalSpeed = originalSpeed;
  }
}
