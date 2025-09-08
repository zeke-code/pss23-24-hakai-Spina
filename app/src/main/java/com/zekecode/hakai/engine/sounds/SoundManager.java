package com.zekecode.hakai.engine.sounds;

import com.google.common.eventbus.Subscribe;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;
import org.yaml.snakeyaml.Yaml;

/**
 * Manages loading and playing all sound effects for the game. It automatically discovers sound
 * files and uses a configuration file (sound-map.yml) to map game events to sounds.
 */
public class SoundManager {

  private final Map<String, AudioClip> soundEffects = new HashMap<>();
  private Map<String, String> eventToSoundMap = new HashMap<>();

  public SoundManager() {
    loadSoundFiles();
    loadSoundMappings();
  }

  /**
   * Listens for ANY event published on the event bus. If the event's class name is registered in
   * our map, it plays the corresponding sound.
   *
   * @param event The event object published on the bus.
   */
  @Subscribe
  public void handleAnyEvent(Object event) {
    String eventClassName = event.getClass().getSimpleName();
    String soundToPlay = eventToSoundMap.get(eventClassName);

    if (soundToPlay != null) {
      playSound(soundToPlay);
    }
  }

  /**
   * Scans the 'resources/sounds' directory for all files and loads them into memory. The name of
   * the sound effect is derived from its filename.
   */
  private void loadSoundFiles() {
    String soundDirectory = "sounds"; // The directory in src/main/resources

    try (ScanResult scanResult = new ClassGraph().acceptPaths(soundDirectory).scan()) {
      ResourceList soundResources = scanResult.getAllResources();
      System.out.println("Discovered " + soundResources.size() + " sound files...");

      for (Resource res : soundResources) {
        String path = res.getPath();
        String filename = path.substring(path.lastIndexOf('/') + 1);
        String soundName = filename.substring(0, filename.lastIndexOf('.'));
        loadSound(soundName, path);
      }
    }
  }

  /** Loads the sound-map.yml file to create the event-to-sound mapping. */
  private void loadSoundMappings() {
    Yaml yaml = new Yaml();
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sound-map.yml");

    if (inputStream == null) {
      System.err.println("CRITICAL: sound-map.yml not found! No sounds will be mapped.");
      return;
    }

    SoundConfig config = yaml.loadAs(inputStream, SoundConfig.class);
    this.eventToSoundMap = config.sounds;
    System.out.println("Loaded " + eventToSoundMap.size() + " event-to-sound mappings.");
  }

  /**
   * Loads a single sound file into an AudioClip and stores it in the soundEffects map.
   *
   * @param name The identifier for the sound effect (derived from filename).
   * @param resourcePath The path to the sound file within resources.
   */
  private void loadSound(String name, String resourcePath) {
    URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      System.err.println("Sound resource not found: " + resourcePath);
      return;
    }
    try {
      AudioClip clip = new AudioClip(resourceUrl.toExternalForm());
      soundEffects.put(name, clip);
      System.out.println("  -> Loaded sound asset '" + name + "'");
    } catch (Exception e) {
      System.err.println("Error loading sound: " + resourcePath);
      e.printStackTrace();
    }
  }

  /**
   * Plays the sound effect associated with the given name.
   *
   * @param name The identifier for the sound effect to play.
   */
  private void playSound(String name) {
    AudioClip clip = soundEffects.get(name);
    if (clip != null) {
      clip.play();
    } else {
      System.err.println(
          "Attempted to play unknown sound: '"
              + name
              + "'. Check sound-map.yml and the sounds/ folder.");
    }
  }
}
