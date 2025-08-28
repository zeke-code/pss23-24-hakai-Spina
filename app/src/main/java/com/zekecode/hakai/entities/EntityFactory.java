package com.zekecode.hakai.entities;

import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import javafx.scene.paint.Color;

/**
 * A factory class dedicated to creating pre-configured game entities. This centralizes entity
 * creation logic, making it easier to manage and modify.
 */
public class EntityFactory {

  private final World world;

  /**
   * Constructs an EntityFactory that will create entities within the given World.
   *
   * @param world The ECS World where entities will be created.
   */
  public EntityFactory(World world) {
    this.world = world;
  }

  /**
   * Creates the player entity (the paddle). The paddle is configured with all necessary components
   * for rendering, physics, and player input.
   *
   * @param x The initial horizontal position.
   * @param y The initial vertical position.
   * @return The fully configured player Entity.
   */
  public Entity createPlayer(double x, double y) {
    Entity player = world.createEntity();

    // Add all the necessary components for the player paddle
    player.addComponent(new PositionComponent(x, y));
    player.addComponent(new VelocityComponent(0, 0)); // Starts stationary
    player.addComponent(new RenderComponent(100, 20, Color.DEEPSKYBLUE));
    player.addComponent(new InputComponent()); // This marks the entity as player-controlled

    return player;
  }

  /**
   * Creates a ball entity. The ball is configured with components for rendering and physics.
   *
   * @param x The initial horizontal position.
   * @param y The initial vertical position.
   * @return The fully configured ball Entity.
   */
  public Entity createBall(double x, double y) {
    Entity ball = world.createEntity();

    // Add all the necessary components for the ball
    ball.addComponent(new PositionComponent(x, y));
    ball.addComponent(new VelocityComponent(200, 200)); // Initial velocity
    ball.addComponent(new RenderComponent(15, 15, Color.ORANGE));
    ball.addComponent(new BallComponent());

    return ball;
  }
}
