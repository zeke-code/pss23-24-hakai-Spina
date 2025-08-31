package com.zekecode.hakai.systems.rendering;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class RenderSystem extends GameSystem {

  private final GraphicsContext gc;
  private final List<EntityRenderer> renderers;

  public RenderSystem(GraphicsContext gc, List<EntityRenderer> renderers) {
    this.gc = gc;
    this.renderers = renderers;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      // We only care about entities that are actually renderable.
      if (entity.hasComponent(PositionComponent.class)
          && entity.hasComponent(RenderComponent.class)) {
        // Find the first renderer that supports this entity and use it.
        for (EntityRenderer renderer : renderers) {
          if (renderer.supports(entity)) {
            renderer.render(gc, entity);
            break;
          }
        }
      }
    }
  }
}
