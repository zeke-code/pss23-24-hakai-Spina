package com.zekecode.hakai.systems.rendering.renderers;

import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.systems.rendering.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class PaddleRenderer implements EntityRenderer {
  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();

    // Using a fixed arc size makes the corners rounded but not fully circular.
    // This is exactly what you asked for!
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

  @Override
  public boolean supports(Entity entity) {
    return entity.hasComponent(InputComponent.class);
  }
}
