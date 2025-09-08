package com.zekecode.hakai.entities.renderers;

import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Renders a paddle entity with a sleek, modern look using gradients and rounded corners to enhance
 * its visual appeal.
 */
public class PaddleRenderer implements EntityRenderer {
  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();

    // Using a fixed arc size makes the corners rounded but not fully circular.
    double arcSize = 15.0;

    // A metallic-looking gradient for the paddle surface
    LinearGradient gradient =
        new LinearGradient(
            0,
            pos.y,
            0,
            pos.y + render.height,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE),
            new Stop(1, Color.DARKGRAY));

    gc.setFill(gradient);
    gc.fillRoundRect(pos.x, pos.y, render.width, render.height, arcSize, arcSize);

    // A border helps define the paddle's shape
    gc.setStroke(Color.GRAY);
    gc.setLineWidth(2);
    gc.strokeRoundRect(pos.x, pos.y, render.width, render.height, arcSize, arcSize);
  }

  /**
   * This renderer supports entities that have an InputComponent, which is characteristic of the
   * player paddle.
   *
   * @param entity The entity to check.
   * @return true if the entity has an InputComponent, false otherwise.
   */
  @Override
  public boolean supports(Entity entity) {
    return entity.hasComponent(InputComponent.class);
  }
}
