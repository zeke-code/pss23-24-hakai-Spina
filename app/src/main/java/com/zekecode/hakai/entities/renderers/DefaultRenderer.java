package com.zekecode.hakai.entities.renderers;

import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;

/**
 * A default renderer that handles any entity with Position and Render components. This renderer
 * serves as a fallback when no other specialized renderer is applicable.
 */
public class DefaultRenderer implements EntityRenderer {
  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();
    gc.setFill(render.color);
    gc.fillRect(pos.x, pos.y, render.width, render.height);
  }

  @Override
  public boolean supports(Entity entity) {
    // This is the fallback, so it always returns true.
    // We will ensure it's the last in our list of renderers.
    return true;
  }
}
