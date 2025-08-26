package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

/**
 * A system that handles rendering entities with Position and Render components onto a JavaFX
 * Canvas. This system iterates through all entities, checks for the required components, and draws
 * them using the provided GraphicsContext.
 */
public class RenderSystem extends GameSystem {

  private final GraphicsContext gc;

  public RenderSystem(GraphicsContext gc) {
    this.gc = gc;
  }

  /**
   * Updates the system by rendering entities with Position and Render components.
   *
   * @param entities The list of entities to process.
   * @param deltaTime The time elapsed since the last update, in seconds.
   */
  @Override
  public void update(List<Entity> entities, double deltaTime) {
    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

    for (Entity entity : entities) {
      // The system only cares about entities that have both Position and Render components
      if (entity.hasComponent(PositionComponent.class)
          && entity.hasComponent(RenderComponent.class)) {
        PositionComponent position = entity.getComponent(PositionComponent.class).get();
        RenderComponent render = entity.getComponent(RenderComponent.class).get();

        // Set the fill color and draw a rectangle at the moment.
        gc.setFill(render.color);
        gc.fillRect(position.x, position.y, render.width, render.height);
      }
    }
  }
}
