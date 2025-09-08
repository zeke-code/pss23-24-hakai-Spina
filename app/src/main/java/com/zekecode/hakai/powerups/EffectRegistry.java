package com.zekecode.hakai.powerups;

import com.zekecode.hakai.entities.EntityFactory;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A central registry that automatically discovers and manages all {@link Effect} implementations in
 * the project. It uses the {@link EffectId} annotation to map string identifiers from level files
 * to concrete effect classes.
 */
public class EffectRegistry {

  private final Map<PowerUpType, Effect> effects = new HashMap<>();
  private final EntityFactory entityFactory;

  public EffectRegistry(EntityFactory entityFactory) {
    this.entityFactory = entityFactory;
    discoverAndRegisterEffects();
  }

  /**
   * Scans the classpath for all classes that implement the {@link Effect} interface and are marked
   * with the {@link EffectId} annotation, then instantiates and registers them.
   */
  private void discoverAndRegisterEffects() {
    String effectPackage = "com.zekecode.hakai.powerups.effects";
    String annotationName = EffectId.class.getName();

    try (ScanResult scanResult =
        new ClassGraph().enableAllInfo().acceptPackages(effectPackage).scan()) {
      ClassInfoList effectClasses =
          scanResult
              .getClassesImplementing(Effect.class.getName())
              .filter(classInfo -> classInfo.hasAnnotation(annotationName));

      effectClasses
          .loadClasses(Effect.class)
          .forEach(
              cls -> {
                try {
                  EffectId annotation = cls.getAnnotation(EffectId.class);
                  String idAsString = annotation.value();
                  Effect instance;

                  // Convert the annotation's string to an enum constant.
                  // This will throw an error at startup if you have a typo in the annotation
                  // or forget to add a new type to the enum.
                  PowerUpType type = PowerUpType.valueOf(idAsString);

                  // Try to instantiate with EntityFactory dependency first.
                  try {
                    instance =
                        cls.getDeclaredConstructor(EntityFactory.class).newInstance(entityFactory);
                  } catch (NoSuchMethodException e) {
                    // Fall back to the default no-arg constructor for simple effects.
                    instance = cls.getDeclaredConstructor().newInstance();
                  }

                  effects.put(type, instance);
                  System.out.println("  -> Discovered and registered effect: " + type);
                } catch (Exception e) {
                  System.err.println("Failed to instantiate effect: " + cls.getName());
                  e.printStackTrace();
                }
              });
    }
    System.out.println("Effect discovery complete. " + effects.size() + " effects loaded.");
  }

  /**
   * Retrieves an effect instance by its unique identifier.
   *
   * @param type The PowerUpType enum value representing the effect's ID.
   * @return An Optional containing the Effect instance if found, otherwise an empty Optional.
   */
  public Optional<Effect> getEffect(PowerUpType type) {
    return Optional.ofNullable(effects.get(type));
  }
}
