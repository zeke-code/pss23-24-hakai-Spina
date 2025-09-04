package com.zekecode.hakai.systems.rendering.renderers;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.PowerUpDropComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.systems.rendering.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class PowerUpRenderer implements EntityRenderer {

  // A glowing effect to make the star pop from the background
  private static final DropShadow glow = new DropShadow(15, Color.GOLD);

  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();

    gc.save();
    gc.setEffect(glow);

    // Create a golden-looking gradient for the star's fill
    LinearGradient goldGradient =
        new LinearGradient(
            pos.x,
            pos.y,
            pos.x,
            pos.y + render.height,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.YELLOW),
            new Stop(1, Color.GOLDENROD));

    gc.setFill(goldGradient);
    drawStar(gc, pos.x, pos.y, render.width, render.height);

    gc.restore();
  }

  /**
   * Calculates the vertices of a 5-pointed star and draws it as a polygon.
   *
   * @param gc The graphics context to draw on.
   * @param x The top-left x coordinate of the bounding box.
   * @param y The top-left y coordinate of the bounding box.
   * @param width The width of the bounding box.
   * @param height The height of the bounding box.
   */
  private void drawStar(GraphicsContext gc, double x, double y, double width, double height) {
    double centerX = x + width / 2;
    double centerY = y + height / 2;
    double outerRadius = Math.min(width, height) / 2;
    double innerRadius = outerRadius / 2.0;
    int numPoints = 5;

    double[] xPoints = new double[numPoints * 2];
    double[] yPoints = new double[numPoints * 2];

    for (int i = 0; i < numPoints * 2; i++) {
      double angle = Math.PI / numPoints * i - Math.PI / 2;
      double radius = (i % 2 == 0) ? outerRadius : innerRadius;
      xPoints[i] = centerX + radius * Math.cos(angle);
      yPoints[i] = centerY + radius * Math.sin(angle);
    }

    gc.fillPolygon(xPoints, yPoints, numPoints * 2);
  }

  @Override
  public boolean supports(Entity entity) {
    return entity.hasComponent(PowerUpDropComponent.class);
  }
}
