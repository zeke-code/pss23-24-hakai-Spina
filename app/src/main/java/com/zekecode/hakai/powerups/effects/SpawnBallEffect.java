package com.zekecode.hakai.powerups.effects;

import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.powerups.Effect;
import com.zekecode.hakai.powerups.EffectId;

@EffectId("SPAWN_BALL")
public class SpawnBallEffect implements Effect {

  private final EntityFactory entityFactory;

  /**
   * Constructs the effect, injecting the factory needed to create new entities.
   *
   * @param entityFactory The world's entity factory.
   */
  public SpawnBallEffect(EntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Applies the effect. The target for an instant brick effect is the brick entity itself.
   *
   * @param target The brick entity that was destroyed.
   */
  @Override
  public void apply(Entity target) {
    target
        .getComponent(PositionComponent.class)
        .ifPresent(
            pos -> {
              // Create a new LAUNCHED ball at the location of the destroyed brick.
              entityFactory.createLaunchedBall(pos.x, pos.y);
              System.out.println(
                  "SpawnBallEffect: Created a new launched ball at (" + pos.x + ", " + pos.y + ")");
            });
  }

  @Override
  public void remove(Entity target) {
    // Not applicable for an instant effect.
  }

  @Override
  public void update(Entity target, double deltaTime) {
    // Not applicable for an instant effect.
  }

  @Override
  public double getDuration() {
    // 0 indicates an instant, non-timed effect.
    return 0;
  }
}
