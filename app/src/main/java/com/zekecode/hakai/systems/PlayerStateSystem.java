package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.BallLostEvent;
import com.zekecode.hakai.engine.events.GameOverEvent;
import java.util.List;

/** Manages the player's state, such as lives, and reacts to game-altering events. */
public class PlayerStateSystem extends GameSystem {

  private final EventBus eventBus;
  private int lives;

  public PlayerStateSystem(int startingLives, EventBus eventBus) {
    this.lives = startingLives;
    this.eventBus = eventBus;
  }

  /** This method listens for the BallLostEvent. */
  @Subscribe
  public void onBallLost(BallLostEvent event) {
    this.lives--;
    System.out.println("LIFE LOST! Lives remaining: " + this.lives);

    if (this.lives <= 0) {
      // When lives run out, post a new, more specific event.
      eventBus.post(new GameOverEvent());
    } else {
      // TODO: If the game is not over, we should reset the ball's position.
      // This would be done by posting a ResetBallEvent, for example.
    }
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {
    // This system is purely event-driven.
  }

  public int getLives() {
    return lives;
  }
}
