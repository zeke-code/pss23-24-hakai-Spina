package com.zekecode.hakai.entities;

import com.zekecode.hakai.components.*;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.ball.BallStuckToPaddleComponent;
import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.components.entities.PlayerStateComponent;
import com.zekecode.hakai.components.entities.ScoreComponent;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.CollidableComponent;
import com.zekecode.hakai.components.physics.MovableComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.physics.VelocityComponent;
import com.zekecode.hakai.components.powerups.PowerUpComponent;
import com.zekecode.hakai.components.powerups.PowerUpDropComponent;
import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.config.data.PowerUpData;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.powerups.EffectCategory;
import com.zekecode.hakai.powerups.PowerUpType;
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

    player.addComponent(new PositionComponent(x, y));
    player.addComponent(new VelocityComponent(0, 0));
    player.addComponent(
        new RenderComponent(
            GameConfig.PADDLE_INITIAL_WIDTH, GameConfig.PADDLE_INITIAL_HEIGHT, Color.WHITE));
    player.addComponent(new CollidableComponent());
    player.addComponent(new InputComponent());
    player.addComponent(new MovableComponent(GameConfig.PADDLE_SPEED));

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

    ball.addComponent(new PositionComponent(x, y));
    ball.addComponent(
        new VelocityComponent(
            GameConfig.BALL_INITIAL_VELOCITY_X, GameConfig.BALL_INITIAL_VELOCITY_Y));
    ball.addComponent(
        new RenderComponent(GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, Color.WHITE));
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

    double randomVelX =
        (Math.random() - 0.5)
            * (GameConfig.BALL_INITIAL_VELOCITY_X * GameConfig.BALL_SPAWN_RANDOMNESS_FACTOR);
    double launchSpeedY = Math.abs(GameConfig.BALL_LAUNCH_VELOCITY_Y);

    ball.addComponent(new PositionComponent(x, y));
    ball.addComponent(new VelocityComponent(randomVelX, launchSpeedY)); // Launch downwards
    ball.addComponent(
        new RenderComponent(GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, Color.WHITE));
    ball.addComponent(new BallComponent());
    ball.addComponent(new CollidableComponent());

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
  public Entity createPowerUpDrop(
      double x, double y, PowerUpType effectType, EffectCategory effectCategory) {
    Entity drop = world.createEntity();
    drop.addComponent(new PositionComponent(x, y));
    drop.addComponent(new VelocityComponent(0, GameConfig.POWERUP_DROP_SPEED));
    drop.addComponent(
        new RenderComponent(
            GameConfig.POWERUP_DROP_WIDTH, GameConfig.POWERUP_DROP_HEIGHT, Color.CYAN));
    drop.addComponent(new PowerUpDropComponent(effectType, effectCategory));
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
    gameState.addComponent(new PlayerStateComponent(GameConfig.PLAYER_STARTING_LIVES));
    gameState.addComponent(new ScoreComponent());
    return gameState;
  }
}
