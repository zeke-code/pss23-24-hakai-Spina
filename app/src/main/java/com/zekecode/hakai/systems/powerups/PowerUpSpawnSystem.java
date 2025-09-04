package com.zekecode.hakai.systems.powerups;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.PowerUpComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.BrickDestroyedEvent;
import com.zekecode.hakai.entities.EntityFactory;
import java.util.List;
import java.util.Optional;

/** Spawns a power-up entity when a brick with a PowerUpComponent is destroyed. */
public class PowerUpSpawnSystem extends GameSystem {

  private final EntityFactory entityFactory;

  public PowerUpSpawnSystem(EntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  @Subscribe
  public void onBrickDestroyed(BrickDestroyedEvent event) {
    Optional<PowerUpComponent> powerUpOpt = event.brickEntity.getComponent(PowerUpComponent.class);
    Optional<PositionComponent> posOpt = event.brickEntity.getComponent(PositionComponent.class);

    if (powerUpOpt.isPresent() && posOpt.isPresent()) {
      PowerUpComponent powerUp = powerUpOpt.get();
      PositionComponent pos = posOpt.get();
      entityFactory.createPowerUpDrop(pos.x, pos.y, powerUp.type);
      System.out.println("Spawning power-up of type: " + powerUp.type);
    }
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven.
  }
}
