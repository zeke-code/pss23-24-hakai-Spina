package com.zekecode.hakai.events;

/** Event representing a change in the player's score. */
public class ScoreChangedEvent {
  public final int newScore;

  public ScoreChangedEvent(int newScore) {
    this.newScore = newScore;
  }
}
