package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/**
 * A component that represents the state of the "Paddle Expansion" power-up. It is attached to the
 * paddle entity while the effect is active.
 */
public class PaddleExpansionComponent implements Component {
  public double timeRemaining;
  public final double originalWidth;
  public static final double DURATION = 10.0; // 10 seconds
  public static final double WIDTH_MULTIPLIER = 1.5;

  public PaddleExpansionComponent(double originalWidth) {
    this.timeRemaining = DURATION;
    this.originalWidth = originalWidth;
  }
}
