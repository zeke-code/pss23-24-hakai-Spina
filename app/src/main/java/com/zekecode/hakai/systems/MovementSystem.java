package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.MovableComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.input.InputManager;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 * A system that updates the velocity of player-controlled entities based on keyboard input. It
 * checks for left/right movement keys and adjusts the entity's horizontal velocity accordingly.
 */
public class MovementSystem extends GameSystem {
  private final InputManager inputManager;

  public MovementSystem(InputManager inputManager) {
    this.inputManager = inputManager;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      // We only care about entities that are player-controlled and movable.
      if (entity.hasComponent(InputComponent.class)) {

        entity
            .getComponent(VelocityComponent.class)
            .ifPresent(
                velocity ->
                    entity
                        .getComponent(MovableComponent.class)
                        .ifPresent(
                            movable -> {
                              double currentSpeed = movable.speed;

                              // Reset horizontal velocity to 0 by default
                              velocity.x = 0;

                              // Apply velocity based on input
                              if (inputManager.isKeyPressed(KeyCode.LEFT)
                                  || inputManager.isKeyPressed(KeyCode.A)) {
                                velocity.x = -currentSpeed;
                              }
                              if (inputManager.isKeyPressed(KeyCode.RIGHT)
                                  || inputManager.isKeyPressed(KeyCode.D)) {
                                velocity.x = currentSpeed;
                              }
                            }));
      }
    }
  }
}
