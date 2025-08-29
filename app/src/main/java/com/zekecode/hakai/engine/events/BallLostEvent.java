package com.zekecode.hakai.engine.events;

/**
 * An event that is published when the ball goes off the bottom of the screen.
 *
 * <p>This is a simple signal event. Systems can listen for it to trigger "lose a life" logic, reset
 * the ball and paddle, or update the game state.
 */
public class BallLostEvent {}
