package com.zekecode.hakai.systems.rendering;

import com.zekecode.hakai.core.Entity;
import javafx.scene.canvas.GraphicsContext;

/**
 * Defines the contract for a rendering strategy. Each implementation will know how to draw a
 * specific type of entity.
 */
public interface EntityRenderer {
  /**
   * Renders the given entity onto the canvas.
   *
   * @param gc The graphics context to draw on.
   * @param entity The entity to be rendered.
   */
  void render(GraphicsContext gc, Entity entity);

  /**
   * Checks if this renderer can handle the given entity.
   *
   * @param entity The entity to check.
   * @return true if this renderer should be used for the entity, false otherwise.
   */
  boolean supports(Entity entity);
}
