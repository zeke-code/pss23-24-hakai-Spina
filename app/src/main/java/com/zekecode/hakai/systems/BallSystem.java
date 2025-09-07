package com.zekecode.hakai.systems;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.*;
import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.ball.BallStuckToPaddleComponent;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.physics.VelocityComponent;
import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.input.InputManager;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.events.ball.ResetBallEvent;
import java.util.List;
import java.util.Optional;
import javafx.scene.input.KeyCode;

/** A system that manages the ball's behavior, including sticking to the paddle and launching. */
public class BallSystem extends GameSystem {

  private final InputManager inputManager;
  private final EntityFactory entityFactory;

  public BallSystem(InputManager inputManager, EntityFactory entityFactory) {
    this.inputManager = inputManager;
    this.entityFactory = entityFactory;
  }

  /**
   * Handles the ResetBallEvent by creating a new ball entity.
   *
   * @param event The ResetBallEvent instance.
   */
  @Subscribe
  public void onResetBall(ResetBallEvent event) {
    System.out.println("ResetBallEvent received. Creating a new ball.");

    // The factory creates a new ball with the BallStuckToPaddleComponent already on it.
    // The exact coordinates don't matter, as the update() loop will snap it to
    // the paddle on the very next frame.
    // We calculate the initial position here using constants for clarity.
    double ballX = (GameConfig.SCREEN_WIDTH / 2.0) - (GameConfig.BALL_WIDTH / 2.0);
    double ballY = GameConfig.SCREEN_HEIGHT / 2.0;
    entityFactory.createBall(ballX, ballY);
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // Find the single paddle entity on every frame.
    Optional<Entity> paddleOpt = findPaddle(entities);
    if (paddleOpt.isEmpty()) {
      return; // If there's no paddle, we can't do anything.
    }
    Entity paddle = paddleOpt.get();

    // Iterate over all entities. If an entity is a ball and is marked as "stuck",
    // process its logic. This is now stateless and supports multiple balls.
    for (Entity ball : entities) {
      if (ball.hasComponent(BallComponent.class)
          && ball.hasComponent(BallStuckToPaddleComponent.class)) {
        stickBallToPaddle(ball, paddle);
        handleLaunch(ball);
      }
    }
  }

  private void stickBallToPaddle(Entity ball, Entity paddle) {
    // This logic runs every frame to keep the ball positioned correctly on the paddle
    Optional<PositionComponent> paddlePosOpt = paddle.getComponent(PositionComponent.class);
    Optional<RenderComponent> paddleRenderOpt = paddle.getComponent(RenderComponent.class);
    Optional<PositionComponent> ballPosOpt = ball.getComponent(PositionComponent.class);
    Optional<RenderComponent> ballRenderOpt = ball.getComponent(RenderComponent.class);

    if (paddlePosOpt.isPresent()
        && paddleRenderOpt.isPresent()
        && ballPosOpt.isPresent()
        && ballRenderOpt.isPresent()) {
      PositionComponent paddlePos = paddlePosOpt.get();
      RenderComponent paddleRender = paddleRenderOpt.get();
      PositionComponent ballPos = ballPosOpt.get();
      RenderComponent ballRender = ballRenderOpt.get();

      ballPos.x = paddlePos.x + (paddleRender.width / 2) - (ballRender.width / 2);
      ballPos.y = paddlePos.y - ballRender.height;
    }
  }

  private void handleLaunch(Entity ball) {
    // Check for the launch input
    if (inputManager.isKeyPressed(KeyCode.W) || inputManager.isKeyPressed(KeyCode.UP)) {
      // 1. Remove the "stuck" state
      ball.removeComponent(BallStuckToPaddleComponent.class);

      // 2. Give the ball an initial velocity to launch it
      ball.getComponent(VelocityComponent.class)
          .ifPresent(
              velocity -> {
                velocity.x = GameConfig.BALL_LAUNCH_VELOCITY_X; // A slight angle
                velocity.y = GameConfig.BALL_LAUNCH_VELOCITY_Y; // Launch upwards
              });
    }
  }

  private Optional<Entity> findPaddle(List<Entity> entities) {
    return entities.stream().filter(e -> e.hasComponent(InputComponent.class)).findFirst();
  }
}
