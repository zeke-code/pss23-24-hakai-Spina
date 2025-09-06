package com.zekecode.hakai.systems.collisions;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.CollidableComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.CollisionEvent;
import java.util.List;

public class CollisionSystem extends GameSystem {

  private final EventBus eventBus;

  public CollisionSystem(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    List<Entity> collidables =
        entities.stream()
            .filter(
                e ->
                    e.hasComponent(CollidableComponent.class)
                        && e.hasComponent(PositionComponent.class)
                        && e.hasComponent(RenderComponent.class))
            .toList();

    for (int i = 0; i < collidables.size(); i++) {
      Entity entityA = collidables.get(i);
      PositionComponent posA = entityA.getComponent(PositionComponent.class).get();
      RenderComponent renderA = entityA.getComponent(RenderComponent.class).get();

      for (int j = i + 1; j < collidables.size(); j++) {
        Entity entityB = collidables.get(j);

        if (entityA.getId() == entityB.getId()) {
          continue;
        }

        if (!entityA.hasComponent(VelocityComponent.class)
            && !entityB.hasComponent(VelocityComponent.class)) {
          continue;
        }

        PositionComponent posB = entityB.getComponent(PositionComponent.class).get();
        RenderComponent renderB = entityB.getComponent(RenderComponent.class).get();

        if (isColliding(posA, renderA, posB, renderB)) {
          eventBus.post(new CollisionEvent(entityA, entityB));
        }
      }
    }
  }

  private boolean isColliding(
      PositionComponent posA,
      RenderComponent renderA,
      PositionComponent posB,
      RenderComponent renderB) {
    return posA.x < posB.x + renderB.width
        && posA.x + renderA.width >= posB.x
        && posA.y < posB.y + renderB.height
        && posA.y + renderA.height >= posB.y;
  }
}
