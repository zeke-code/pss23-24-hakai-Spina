package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.BallComponent;
import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import java.util.List;

/**
 * A system that handles the physics of the game, including movement and collision detection.
 *
 * <p>This system processes entities with PositionComponent and VelocityComponent to update their
 * positions based on their velocities. It also implements specific rules for different types of
 * entities, such as bouncing the ball off walls and constraining the player's paddle within screen
 * boundaries.
 */
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
      // This logic will only run for entities that have both a position and velocity.
      entity
          .getComponent(PositionComponent.class)
          .ifPresent(
              position ->
                  entity
                      .getComponent(VelocityComponent.class)
                      .ifPresent(
                          velocity -> {
                            // First, apply the movement for this frame.
                            position.x += velocity.x * deltaTime;
                            position.y += velocity.y * deltaTime;

                            // --- Rule for the Ball ---
                            if (entity.hasComponent(BallComponent.class)) {
                              // We need the render component to know the ball's size.
                              entity
                                  .getComponent(RenderComponent.class)
                                  .ifPresent(
                                      render ->
                                          handleBallWallCollision(position, velocity, render));
                            }

                            // --- Rule for the Player Paddle ---
                            if (entity.hasComponent(InputComponent.class)) {
                              entity
                                  .getComponent(RenderComponent.class)
                                  .ifPresent(render -> handlePaddleWallCollision(position, render));
                            }
                          }));
    }
  }

  /**
   * Clamps the player's paddle to the screen boundaries so it cannot go off-screen.
   *
   * @param position The paddle's PositionComponent.
   * @param render The paddle's RenderComponent to get its width.
   */
  private void handlePaddleWallCollision(PositionComponent position, RenderComponent render) {
    if (position.x < 0) {
      position.x = 0;
    }
    if (position.x + render.width > screenWidth) {
      position.x = screenWidth - render.width;
    }
  }

  /**
   * Handles the ball's bouncing behavior when it collides with the screen edges.
   *
   * @param position The ball's PositionComponent.
   * @param velocity The ball's VelocityComponent.
   * @param render The ball's RenderComponent to get its size.
   */
  private void handleBallWallCollision(
      PositionComponent position, VelocityComponent velocity, RenderComponent render) {
    // Bounce off left or right wall
    if (position.x <= 0 && velocity.x < 0) {
      velocity.x *= -1; // Reverse horizontal velocity
    }
    if (position.x + render.width >= screenWidth && velocity.x > 0) {
      velocity.x *= -1; // Reverse horizontal velocity
    }

    // Bounce off top wall
    if (position.y <= 0 && velocity.y < 0) {
      velocity.y *= -1; // Reverse vertical velocity
    }

    // TODO: In a later step, we need to handle the ball going off the bottom of the screen.
    if (position.y + render.height >= screenHeight) {
      // For now, let's just make it bounce off the bottom too for testing.
      // We will change this logic later.
      velocity.y *= -1;
    }
  }
}
