package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/**
 * A component specifically for storing the original state of the paddle before a power-up effect is
 * applied. This ensures a clean separation of concerns and makes it easy to revert changes.
 */
public class PaddleStateComponent implements Component {
  public final double originalWidth;

  public PaddleStateComponent(double originalWidth) {
    this.originalWidth = originalWidth;
  }
}
