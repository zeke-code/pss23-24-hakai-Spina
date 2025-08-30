package com.zekecode.hakai.systems;

import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.input.InputManager;
import java.util.List;
import java.util.Optional;
import javafx.scene.input.KeyCode;

/** Manages the state of the ball, including sticking it to the paddle and launching it. */
public class BallSystem extends GameSystem {

  private final InputManager inputManager;

  public BallSystem(InputManager inputManager) {
    this.inputManager = inputManager;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    Optional<Entity> paddleOpt = findPaddle(entities);
    if (!paddleOpt.isPresent()) {
      return;
    }
    Entity paddle = paddleOpt.get();

    for (Entity ball : entities) {
      // Find any ball that is currently stuck to the paddle
      if (ball.hasComponent(BallStuckToPaddleComponent.class)) {
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
                velocity.x = 50; // A slight angle
                velocity.y = -350; // Launch upwards
              });
    }
  }

  private Optional<Entity> findPaddle(List<Entity> entities) {
    for (Entity entity : entities) {
      if (entity.hasComponent(InputComponent.class)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
