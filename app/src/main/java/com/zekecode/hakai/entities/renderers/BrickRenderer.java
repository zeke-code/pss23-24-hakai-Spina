package com.zekecode.hakai.entities.renderers;

import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Renders brick entities with a distinct style, including a border to make them visually stand out.
 * This renderer is specifically designed for entities that have a BrickComponent.
 */
public class BrickRenderer implements EntityRenderer {
  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();

    Color baseColor = render.color;
    Color borderColor = baseColor.darker();

    gc.setFill(baseColor);
    gc.fillRect(pos.x, pos.y, render.width, render.height);

    gc.setStroke(borderColor);
    gc.setLineWidth(3);
    gc.strokeRect(pos.x, pos.y, render.width, render.height);
  }

  @Override
  public boolean supports(Entity entity) {
    return entity.hasComponent(BrickComponent.class);
  }
}
