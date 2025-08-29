package com.zekecode.hakai.engine;

import com.zekecode.hakai.engine.data.LevelData;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;

/**
 * LevelLoader is responsible for loading level data from YAML files located in the resources/levels
 * directory.
 */
public class LevelLoader {

  public LevelData loadLevel(String levelFileName) {
    Yaml yaml = new Yaml();

    InputStream inputStream =
        this.getClass().getClassLoader().getResourceAsStream("levels/" + levelFileName);

    if (inputStream == null) {
      throw new IllegalArgumentException("Level file not found in resources: " + levelFileName);
    }

    return yaml.loadAs(inputStream, LevelData.class);
  }
}
