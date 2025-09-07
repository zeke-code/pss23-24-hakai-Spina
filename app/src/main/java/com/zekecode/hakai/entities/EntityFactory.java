package com.zekecode.hakai.entities;

import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.engine.data.PowerUpData;
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
    player.addComponent(new RenderComponent(100, 20, Color.WHITE));
    player.addComponent(new CollidableComponent());
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
    ball.addComponent(new RenderComponent(15, 15, Color.WHITE));
    ball.addComponent(new BallComponent());
    ball.addComponent(new CollidableComponent());
    ball.addComponent(new BallStuckToPaddleComponent());

    return ball;
  }

  /**
   * Creates a new ball that is immediately active and launched with its own velocity. Used by
   * power-ups.
   *
   * @param x The initial horizontal position (from the brick).
   * @param y The initial vertical position (from the brick).
   * @return The fully configured and launched ball Entity.
   */
  public Entity createLaunchedBall(double x, double y) {
    Entity ball = world.createEntity();

    // A random horizontal velocity makes the spawn more dynamic.
    double randomVelX = (Math.random() - 0.5) * 250;

    ball.addComponent(new PositionComponent(x, y));
    ball.addComponent(new VelocityComponent(randomVelX, 250)); // Launch downwards
    ball.addComponent(new RenderComponent(15, 15, Color.WHITE));
    ball.addComponent(new BallComponent());
    ball.addComponent(new CollidableComponent());
    // NOTE: We DO NOT add the BallStuckToPaddleComponent.

    return ball;
  }

  /**
   * Creates a brick entity. Bricks are static and only need position, rendering, and health
   * components.
   *
   * @param x The initial horizontal position.
   * @param y The initial vertical position.
   * @param width The width of the brick.
   * @param height The height of the brick.
   * @param color The color of the brick.
   * @param hp The hit points (durability) of the brick.
   * @return The fully configured brick Entity.
   */
  public Entity createBrick(
      double x,
      double y,
      double width,
      double height,
      Color color,
      int hp,
      PowerUpData powerUpData) {
    Entity brick = world.createEntity();

    brick.addComponent(new PositionComponent(x, y));
    brick.addComponent(new RenderComponent(width, height, color));
    brick.addComponent(new BrickComponent(hp));
    brick.addComponent(new CollidableComponent());

    if (powerUpData != null) {
      brick.addComponent(new PowerUpComponent(powerUpData));
    }

    return brick;
  }

  /**
   * Creates a falling power-up item that can be collected by the player.
   *
   * @param x The initial x position (usually from a destroyed brick).
   * @param y The initial y position (usually from a destroyed brick).
   * @param effectType The type of effect to be applied on collection.
   * @return The fully configured power-up drop Entity.
   */
  public Entity createPowerUpDrop(double x, double y, String effectType) {
    Entity drop = world.createEntity();
    drop.addComponent(new PositionComponent(x, y));
    drop.addComponent(new VelocityComponent(0, 150)); // Move downwards
    drop.addComponent(new RenderComponent(40, 15, Color.CYAN));
    drop.addComponent(new PowerUpDropComponent(effectType));
    drop.addComponent(new CollidableComponent());
    return drop;
  }

  /**
   * Creates a single entity to track global game state, such as score and lives.
   *
   * @return The game state Entity.
   */
  public Entity createPlayerState() {
    Entity gameState = world.createEntity();
    gameState.addComponent(new PlayerStateComponent(3));
    gameState.addComponent(new ScoreComponent());
    return gameState;
  }
}
