package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/**
 * A marker component indicating that an entity can participate in collision detection and response.
 * Entities with this component will be considered by the CollisionSystem when checking for
 * overlaps.
 */
public class CollidableComponent implements Component {}
