// Create this new file: app/src/main/java/com/zekecode/hakai/components/ScoreComponent.java
package com.zekecode.hakai.components.entities;

import com.zekecode.hakai.core.Component;

/**
 * A component that holds the player's current score. Designed to be on a single entity that
 * represents global game state.
 */
public class ScoreComponent implements Component {
  public int score = 0;
}
