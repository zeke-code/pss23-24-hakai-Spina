package com.zekecode.hakai.engine.fx;

import com.google.common.eventbus.Subscribe;
import com.zekecode.hakai.engine.events.BallLostEvent;
import com.zekecode.hakai.engine.events.BrickHitEvent;
import com.zekecode.hakai.engine.events.PaddleHitEvent;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

/**
 * Manages loading and playing all sound effects for the game. It automatically discovers sound
 * files from the resources/sounds directory and listens for game events to play the corresponding
 * sounds.
 */
public class SoundManager {

  private final Map<String, AudioClip> soundEffects = new HashMap<>();

  public SoundManager() {
    loadSounds();
  }

  /**
   * Scans the 'resources/sounds' directory for all files and loads them. The name of the sound
   * effect is derived from its filename (e.g., "paddle_hit.wav" becomes the sound named
   * "paddle_hit").
   */
  private void loadSounds() {
    String soundDirectory = "sounds"; // The directory in src/main/resources

    try (ScanResult scanResult = new ClassGraph().acceptPaths(soundDirectory).scan()) {
      ResourceList soundResources = scanResult.getAllResources();

      System.out.println("Discovered " + soundResources.size() + " sound files...");

      for (Resource res : soundResources) {
        String path = res.getPath();
        // Get the filename from the full path (e.g., "sounds/paddle_hit.wav" -> "paddle_hit.wav")
        String filename = path.substring(path.lastIndexOf('/') + 1);
        String soundName = filename.substring(0, filename.lastIndexOf('.'));

        loadSound(soundName, path);
      }
    }
  }

  private void loadSound(String name, String resourcePath) {
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      System.err.println("Sound resource not found: " + resourcePath);
      return;
    }
    try {
      AudioClip clip = new AudioClip(resourceUrl.toExternalForm());
      soundEffects.put(name, clip);
      System.out.println("  -> Loaded sound '" + name + "'");
    } catch (Exception e) {
      System.err.println("Error loading sound: " + resourcePath);
      e.printStackTrace();
    }
  }

  private void playSound(String name) {
    AudioClip clip = soundEffects.get(name);
    if (clip != null) {
      clip.play();
    } else {
      // This is a helpful warning for developers
      System.err.println("Attempted to play unknown sound: '" + name + "'");
    }
  }

  // --- Event Subscribers ---

  @Subscribe
  public void onPaddleHit(PaddleHitEvent event) {
    playSound("paddle_hit");
  }

  @Subscribe
  public void onBrickHit(BrickHitEvent event) {
    playSound("brick_hit");
  }

  @Subscribe
  public void onBallLost(BallLostEvent event) {
    playSound("ball_lost");
  }

  //    @Subscribe
  //    public void onGameOver(GameOverEvent event) {
  //        playSound("game_over");
  //    }
}
