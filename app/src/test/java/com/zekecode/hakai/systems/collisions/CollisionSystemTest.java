package com.zekecode.hakai.systems.collisions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.graphics.RenderComponent;
import com.zekecode.hakai.components.physics.CollidableComponent;
import com.zekecode.hakai.components.physics.PositionComponent;
import com.zekecode.hakai.components.physics.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.events.CollisionEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollisionSystemTest {

  @Mock private EventBus eventBus;

  private CollisionSystem collisionSystem;
  private Entity entityA;
  private Entity entityB;

  @BeforeEach
  void setUp() {
    collisionSystem = new CollisionSystem(eventBus);

    // Setup two basic collidable entities (A and B)
    entityA = createCollidableEntity(1, 0, 0, 10, 10);
    entityB = createCollidableEntity(2, 5, 5, 10, 10);
  }

  /** Helper to create an entity with Position, Render, and Collidable components. */
  private Entity createCollidableEntity(int id, double x, double y, double width, double height) {
    Entity entity = new Entity(id);
    entity.addComponent(new PositionComponent(x, y));
    entity.addComponent(new RenderComponent(width, height, null));
    entity.addComponent(new CollidableComponent());
    // Add a velocity component to ensure they are checked by the new CollisionSystem logic
    entity.addComponent(new VelocityComponent(1, 1));
    return entity;
  }

  // --- Collision Success Tests (Overlap) ---

  @Test
  void update_overlappingEntities_shouldPostCollisionEvent() {
    // ARRANGE: entities are overlapping (A: 0,0, B: 5,5)

    // ACT
    collisionSystem.update(List.of(entityA, entityB), 0.0);

    // ASSERT
    ArgumentCaptor<CollisionEvent> eventCaptor = ArgumentCaptor.forClass(CollisionEvent.class);
    verify(eventBus, times(1)).post(eventCaptor.capture());

    // Verify the entities in the event (order doesn't strictly matter here, but we check both)
    CollisionEvent capturedEvent = eventCaptor.getValue();
    assertTrue(
        (capturedEvent.entityA == entityA && capturedEvent.entityB == entityB)
            || (capturedEvent.entityA == entityB && capturedEvent.entityB == entityA));
  }

  @Test
  void update_barelyTouchingEntities_shouldPostCollisionEvent() {
    // ARRANGE: Entity B is placed exactly touching the right edge of Entity A.
    // A: (0, 0) W:10, H:10
    // B: (10, 0) W:10, H:10
    entityB.getComponent(PositionComponent.class).get().x = 10;
    entityB.getComponent(PositionComponent.class).get().y = 0;

    // ACT
    collisionSystem.update(List.of(entityA, entityB), 0.0);

    // ASSERT: Barely touching counts as colliding.
    verify(eventBus, times(1)).post(any(CollisionEvent.class));
  }

  // --- Collision Failure Tests (Separation) ---

  @Test
  void update_separatedEntities_shouldNotPostCollisionEvent() {
    // ARRANGE: Entity B is placed far from Entity A.
    // A: (0, 0) W:10, H:10
    // B: (50, 50) W:10, H:10
    entityB.getComponent(PositionComponent.class).get().x = 50;
    entityB.getComponent(PositionComponent.class).get().y = 50;

    // ACT
    collisionSystem.update(List.of(entityA, entityB), 0.0);

    // ASSERT
    verifyNoInteractions(eventBus);
  }

  @Test
  void update_entityChecksSelf_shouldNotPostCollisionEvent() {
    // ARRANGE: List contains two references to the same entity.
    Entity selfColliding = createCollidableEntity(10, 100, 100, 10, 10);
    selfColliding.addComponent(new VelocityComponent(1, 1));

    // ACT
    collisionSystem.update(List.of(selfColliding, selfColliding), 0.0);

    // ASSERT
    verifyNoInteractions(eventBus);
  }

  @Test
  void update_onlyStaticEntitiesExist_shouldNotRunChecks() {
    // ARRANGE: Entities have Collidable, but no VelocityComponent (like two bricks)
    Entity staticA = createCollidableEntity(1, 0, 0, 10, 10);
    staticA.removeComponent(VelocityComponent.class);
    Entity staticB = createCollidableEntity(2, 5, 5, 10, 10);
    staticB.removeComponent(VelocityComponent.class);

    // ACT
    collisionSystem.update(List.of(staticA, staticB), 0.0);

    // ASSERT: Only entities with Velocity should be checked against the collision pair.
    verifyNoInteractions(eventBus);
  }
}
