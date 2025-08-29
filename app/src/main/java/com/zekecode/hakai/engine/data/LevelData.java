package com.zekecode.hakai.engine.data;

import java.util.Map;

/** Data class representing the structure of a game level. */
public class LevelData {
  public String levelName;
  public int levelNumber;
  public LayoutData layout;
  public Map<Character, BrickTypeData> brickTypes;
}
