package com.zekecode.hakai.engine.events;

/** Event representing a change in the player's remaining lives. */
public class LivesChangedEvent {
  public final int remainingLives;

  public LivesChangedEvent(int remainingLives) {
    this.remainingLives = remainingLives;
  }
}
