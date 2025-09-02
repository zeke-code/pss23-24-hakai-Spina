package com.zekecode.hakai.systems;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.*;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.ResetBallEvent;
import com.zekecode.hakai.engine.input.InputManager;
import java.util.List;
import java.util.Optional;
import javafx.scene.input.KeyCode;

/** Manages the state of the ball, including sticking it to the paddle and launching it. */
public class BallSystem extends GameSystem {

  private final InputManager inputManager;
  // Cache the ball and paddle entities for performance.
  private Entity cachedBall;
  private Entity cachedPaddle;

  public BallSystem(InputManager inputManager) {
    this.inputManager = inputManager;
  }

  @Subscribe
  public void onResetBall(ResetBallEvent event) {
    if (this.cachedBall != null) {
      // 1. Add the component that makes the ball stick to the paddle.
      cachedBall.addComponent(new BallStuckToPaddleComponent());

      // 2. Reset its velocity to zero. The launch logic will set a new velocity.
      cachedBall
          .getComponent(VelocityComponent.class)
          .ifPresent(
              vel -> {
                vel.x = 0;
                vel.y = 0;
              });
      System.out.println("Ball has been reset to the paddle.");
    }
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // Find and cache the entities on the first run or if they've been destroyed.
    if (cachedPaddle == null) {
      findPaddle(entities).ifPresent(p -> this.cachedPaddle = p);
    }
    if (cachedBall == null) {
      findBall(entities).ifPresent(b -> this.cachedBall = b);
    }

    // If there's no paddle or ball, there's nothing to do.
    if (cachedPaddle == null || cachedBall == null) {
      return;
    }

    // If the ball has the "stuck" component, process its logic.
    if (cachedBall.hasComponent(BallStuckToPaddleComponent.class)) {
      stickBallToPaddle(cachedBall, cachedPaddle);
      handleLaunch(cachedBall);
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

  private Optional<Entity> findBall(List<Entity> entities) {
    return entities.stream().filter(e -> e.hasComponent(BallComponent.class)).findFirst();
  }

  private Optional<Entity> findPaddle(List<Entity> entities) {
    return entities.stream().filter(e -> e.hasComponent(InputComponent.class)).findFirst();
  }
}
