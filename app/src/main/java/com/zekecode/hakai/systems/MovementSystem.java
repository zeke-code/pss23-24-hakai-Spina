package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.InputComponent;
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
  private static final double PADDLE_SPEED = 400.0; // pixels per second

  public MovementSystem(InputManager inputManager) {
    this.inputManager = inputManager;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    for (Entity entity : entities) {
      // We only care about entities that are player-controlled.
      if (entity.hasComponent(InputComponent.class)) {

        // The code inside the lambda will only execute if the entity has a VelocityComponent.
        entity
            .getComponent(VelocityComponent.class)
            .ifPresent(
                velocity -> {

                  // Reset horizontal velocity to 0 by default
                  velocity.x = 0;

                  // Apply velocity based on input
                  if (inputManager.isKeyPressed(KeyCode.LEFT)
                      || inputManager.isKeyPressed(KeyCode.A)) {
                    velocity.x = -PADDLE_SPEED;
                  }
                  if (inputManager.isKeyPressed(KeyCode.RIGHT)
                      || inputManager.isKeyPressed(KeyCode.D)) {
                    velocity.x = PADDLE_SPEED;
                  }
                });
      }
    }
  }
}
