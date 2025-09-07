package com.zekecode.hakai.utils;

/**
 * A utility class to hold all centralized game configuration constants. This prevents "magic
 * numbers" from being scattered throughout the codebase, making the game easier to tune and
 * maintain.
 */
public final class GameConfig {

  /** Private constructor to prevent this utility class from being instantiated. */
  private GameConfig() {}

  // --- SCREEN DIMENSIONS ---
  public static final int SCREEN_WIDTH = 800;
  public static final int SCREEN_HEIGHT = 600;

  // --- PLAYER & PADDLE SETTINGS ---
  public static final double PADDLE_INITIAL_WIDTH = 100.0;
  public static final double PADDLE_INITIAL_HEIGHT = 20.0;
  public static final double PADDLE_SPEED = 400.0;
  public static final int PLAYER_STARTING_LIVES = 3;
  public static final double PADDLE_Y_OFFSET = 50.0; // Distance from the bottom of the screen

  // --- BALL SETTINGS ---
  public static final double BALL_WIDTH = 15.0;
  public static final double BALL_HEIGHT = 15.0;
  public static final double BALL_INITIAL_VELOCITY_X = 200.0;
  public static final double BALL_INITIAL_VELOCITY_Y = 200.0;
  public static final double BALL_LAUNCH_VELOCITY_X = 50.0;
  public static final double BALL_LAUNCH_VELOCITY_Y = -350.0;
  public static final double BALL_SPIN_FACTOR = 2.0; // How much spin the paddle adds
  public static final double BALL_SPAWN_RANDOMNESS_FACTOR = 1.25; // For power-up spawns

  // --- POWER-UP SETTINGS ---
  public static final double POWERUP_DROP_SPEED = 150.0;
  public static final double POWERUP_DROP_WIDTH = 40.0;
  public static final double POWERUP_DROP_HEIGHT = 15.0;

  // --- GAMEPLAY SETTINGS ---
  public static final int POINTS_PER_BRICK = 10;
}
