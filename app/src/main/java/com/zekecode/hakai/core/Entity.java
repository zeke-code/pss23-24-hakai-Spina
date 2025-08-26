package com.zekecode.hakai.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an entity in the ECS architecture. An entity is a container for components that define
 * its properties and behavior.
 */
public class Entity {
  private final int id;
  private final Map<Class<? extends Component>, Component> components = new HashMap<>();

  public Entity(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void addComponent(Component component) {
    components.put(component.getClass(), component);
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> Optional<T> getComponent(Class<T> componentClass) {
    return Optional.ofNullable((T) components.get(componentClass));
  }

  public boolean hasComponent(Class<? extends Component> componentClass) {
    return components.containsKey(componentClass);
  }
}
