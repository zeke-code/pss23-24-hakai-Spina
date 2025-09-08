package com.zekecode.hakai.core;

/**
 * Marker interface for components in the ECS architecture. Components are data holders (POJOs) that
 * represent various attributes or properties of entities. They do not contain behavior or logic;
 * instead, they are used by systems to process entities. Examples of components include Position,
 * Velocity, Health, and Renderable.
 */
public interface Component {}
