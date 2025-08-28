package com.zekecode.hakai.entities;

import com.zekecode.hakai.components.InputComponent;
import com.zekecode.hakai.components.PositionComponent;
import com.zekecode.hakai.components.RenderComponent;
import com.zekecode.hakai.components.VelocityComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
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
    player.addComponent(new RenderComponent(100, 20, Color.DEEPSKYBLUE));
    player.addComponent(new InputComponent()); // This marks the entity as player-controlled

    return player;
  }
}
