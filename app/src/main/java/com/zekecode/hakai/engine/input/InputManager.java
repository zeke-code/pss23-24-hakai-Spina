package com.zekecode.hakai.engine.input;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

/**
 * A simple class to track the state of keyboard inputs. Systems can query this manager to see which
 * keys are currently pressed without needing access to JavaFX events.
 */
public class InputManager {

  private final Set<KeyCode> pressedKeys = new HashSet<>();

  public void pressKey(KeyCode code) {
    pressedKeys.add(code);
  }

  public void releaseKey(KeyCode code) {
    pressedKeys.remove(code);
  }

  public boolean isKeyPressed(KeyCode code) {
    return pressedKeys.contains(code);
  }
}
