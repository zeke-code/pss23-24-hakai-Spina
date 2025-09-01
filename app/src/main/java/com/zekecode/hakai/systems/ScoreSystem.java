package com.zekecode.hakai.systems;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.GameSystem;
import com.zekecode.hakai.engine.events.BrickDestroyedEvent;
import com.zekecode.hakai.engine.events.ScoreChangedEvent;
import java.util.List;

public class ScoreSystem extends GameSystem {
  private int score = 0;
  private EventBus eventBus;

  public ScoreSystem(EventBus eventBus) {
    this.score = 0;
    this.eventBus = eventBus;
  }

  @Subscribe
  public void onBrickDestroyed(BrickDestroyedEvent event) {
    // We can make this smarter later. For example, by getting the brick's
    // type from its components and awarding different point values.
    this.score += 10;
    System.out.println("SCORE: " + this.score);

    eventBus.post(new ScoreChangedEvent(score));
  }

  @Override
  public void update(List<Entity> entities, double deltaTime) {}

  public int getScore() {
    return score;
  }
}
