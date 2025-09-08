package com.zekecode.hakai.entities.renderers;

import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.powerups.PowerUpDropComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Renders power-up entities with distinct visual styles based on their effect type. Positive
 * power-ups are rendered as glowing stars, while negative power-ups (maluses) are rendered as
 * downward-pointing triangles with a red glow.
 */
public class PowerUpRenderer implements EntityRenderer {

  private static final DropShadow GOOD_GLOW = new DropShadow(15, Color.GOLD);
  private static final DropShadow BAD_GLOW = new DropShadow(15, Color.RED);

  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();
    PowerUpDropComponent drop = entity.getComponent(PowerUpDropComponent.class).get();

    gc.save();

    // Check the effect type to decide how to render it
    switch (drop.category) {
      case POSITIVE:
        renderPowerUp(gc, pos, render);
        break;
      case NEGATIVE:
        renderMalus(gc, pos, render);
        break;
    }

    gc.restore();
  }

  /** Renders a positive power-up as a glowing star. */
  private void renderPowerUp(GraphicsContext gc, PositionComponent pos, RenderComponent render) {
    gc.setEffect(GOOD_GLOW);

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
  }

  /** Renders a negative power-up (malus) as a downward-pointing triangle with a red glow. */
  private void renderMalus(GraphicsContext gc, PositionComponent pos, RenderComponent render) {
    gc.setEffect(BAD_GLOW);

    LinearGradient redGradient =
        new LinearGradient(
            pos.x,
            pos.y,
            pos.x + render.width,
            pos.y + render.height,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.CRIMSON),
            new Stop(1, Color.DARKRED));

    gc.setFill(redGradient);
    // Render as a simple, downward-pointing triangle
    double[] xPoints = {pos.x, pos.x + render.width, pos.x + render.width / 2};
    double[] yPoints = {pos.y, pos.y, pos.y + render.height};
    gc.fillPolygon(xPoints, yPoints, 3);
  }

  /** Draws a star shape centered within the specified rectangle. */
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
