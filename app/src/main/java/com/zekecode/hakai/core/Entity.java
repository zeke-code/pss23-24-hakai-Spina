package com.zekecode.hakai.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an entity in the ECS architecture. An entity is a container for components that define
 * its properties and behavior.
 */
public class Entity {

  /** The unique identifier for this entity. */
  private final int id;

  /** A map to hold components associated with this entity, keyed by their class type. */
  private final Map<Class<? extends Component>, Component> components = new HashMap<>();

  /**
   * Constructs a new Entity with the specified unique identifier.
   *
   * @param id The unique identifier for this entity.
   */
  public Entity(int id) {
    this.id = id;
  }

  /**
   * Returns the unique identifier of this entity.
   *
   * @return The entity's ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Adds a component to this entity. If a component of the same type already exists, it will be
   * replaced.
   *
   * @param component The component to add.
   */
  public void addComponent(Component component) {
    components.put(component.getClass(), component);
  }

  /**
   * Retrieves a component of the specified type from this entity.
   *
   * @param componentClass The class of the component to retrieve.
   * @param <T> The type of the component.
   * @return An Optional containing the component if it exists, or empty if it does not.
   */
  @SuppressWarnings("unchecked")
  public <T extends Component> Optional<T> getComponent(Class<T> componentClass) {
    return Optional.ofNullable((T) components.get(componentClass));
  }

  /**
   * Checks if this entity has a component of the specified type.
   *
   * @param componentClass The class of the component to check for.
   * @return true if the component exists, false otherwise.
   */
  public boolean hasComponent(Class<? extends Component> componentClass) {
    return components.containsKey(componentClass);
  }
}
