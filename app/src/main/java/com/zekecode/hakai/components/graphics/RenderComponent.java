package com.zekecode.hakai.components.graphics;

import com.zekecode.hakai.core.Component;
import javafx.scene.paint.Color;

/**
 * A component that represents the rendering properties of an entity, including its width, height,
 * and color. In future implementations, we need to add texture support.
 */
public class RenderComponent implements Component {
  public double width;
  public double height;
  public Color color;

  public RenderComponent(double width, double height, Color color) {
    this.width = width;
    this.height = height;
    this.color = color;
  }
}
