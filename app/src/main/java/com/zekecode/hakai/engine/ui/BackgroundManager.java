package com.zekecode.hakai.engine.ui;

import java.io.InputStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** Manages loading and rendering a single image file (static or animated) as a background. */
public class BackgroundManager {

  private final Image backgroundImage;
  private final double screenWidth;
  private final double screenHeight;

  public BackgroundManager(String imagePath, double screenWidth, double screenHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;

    // Load the image from the resources folder. JavaFX's Image class handles the format.
    InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath);
    if (imageStream == null) {
      throw new IllegalArgumentException("Background image not found in resources: " + imagePath);
    }
    this.backgroundImage = new Image(imageStream);
  }

  // Draws the current frame of the image to the canvas.
  public void render(GraphicsContext gc) {
    if (backgroundImage != null) {
      gc.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight);
    }
  }
}
