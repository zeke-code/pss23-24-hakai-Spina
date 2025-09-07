package com.zekecode.hakai.entities.renderers;

import com.zekecode.hakai.components.ball.BallComponent;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityRenderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class BallRenderer implements EntityRenderer {
  @Override
  public void render(GraphicsContext gc, Entity entity) {
    PositionComponent pos = entity.getComponent(PositionComponent.class).get();
    RenderComponent render = entity.getComponent(RenderComponent.class).get();

    // Main gradient gives the ball its spherical shape
    RadialGradient gradient =
        new RadialGradient(
            0,
            0,
            pos.x + render.width / 2,
            pos.y + render.height / 2,
            render.width / 2,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.WHITE),
            new Stop(1, Color.LIGHTGRAY));

    gc.setFill(gradient);
    gc.fillOval(pos.x, pos.y, render.width, render.height);

    // A small "specular highlight" makes it look glossy
    double highlightSize = render.width * 0.3;
    gc.setFill(Color.web("white", 0.7)); // Semi-transparent white
    gc.fillOval(pos.x + highlightSize, pos.y + highlightSize, highlightSize, highlightSize);
  }

  @Override
  public boolean supports(Entity entity) {
    return entity.hasComponent(BallComponent.class);
  }
}
