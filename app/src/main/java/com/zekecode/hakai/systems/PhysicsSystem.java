package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;

public class PhysicsSystem extends GameSystem {
  private final double screenWidth;
  private final double screenHeight;

  public PhysicsSystem(double screenWidth, double screenHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {

      entity
          .getComponent(PositionComponent.class)
          .ifPresent(
              position -> {
                entity
                    .getComponent(VelocityComponent.class)
                    .ifPresent(
                        velocity -> {

                          // This inner block is only reached if the entity has BOTH
                          // a PositionComponent and a VelocityComponent.

                          // Update position based on velocity.
                          position.x += velocity.x * deltaTime;
                          position.y += velocity.y * deltaTime;

                          // For boundary checks, we can nest another check for the RenderComponent.
                          entity
                              .getComponent(RenderComponent.class)
                              .ifPresent(
                                  render -> {
                                    if (position.x < 0) {
                                      position.x = 0;
                                    }
                                    if (position.x + render.width > screenWidth) {
                                      position.x = screenWidth - render.width;
                                    }
                                  });
                        });
              });
    }
  }
}
