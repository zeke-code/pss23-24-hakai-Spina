package com.zekecode.hakai.config.data;

import java.util.Map;

/** Data class representing the structure of a game level. */
public class LevelData {
  public String levelName;
  public int levelNumber;
  public String background;
  public LayoutData layout;
  public Map<Character, BrickTypeData> brickTypes;
}
