package com.zekecode.hakai.components;

import com.zekecode.hakai.core.Component;

/**
 * A component that represents the velocity of an entity in 2D space.
 */
public class VelocityComponent implements Component {
    public double x;
    public double y;

    public VelocityComponent(double x, double y) {
        this.x = x;
        this.y = y;
    }
}