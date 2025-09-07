package com.zekecode.hakai.engine.data;

/**
 * A class representing the data for a specific type of brick. This data is typically loaded from a
 * YAML configuration file.
 */
public class BrickTypeData {
  public String color;
  public int hp;

  // We use the wrapper types (Double) instead of primitive (double).
  // This allows them to be 'null' if they are not specified in the YAML file.
  public Double width;
  public Double height;
  public PowerUpData powerUp;
}
