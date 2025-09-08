package com.zekecode.hakai.entities;

import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.entities.renderers.DefaultRenderer;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Comparator;
import java.util.List;

/**
 * Factory class to discover and instantiate all EntityRenderer implementations using ClassGraph.
 */
public class RendererFactory {

  /**
   * Scans the specified package for all classes implementing EntityRenderer, instantiates them, and
   * returns a list of renderers sorted to ensure DefaultRenderer is last.
   *
   * @return List of instantiated EntityRenderer objects.
   */
  public static List<EntityRenderer> createRenderers() {
    String rendererPackage = GameConfig.ENTITIY_RENDERERS_PACKAGE;

    try (ScanResult scanResult =
        new ClassGraph().enableAllInfo().acceptPackages(rendererPackage).scan()) {

      ClassInfoList rendererClasses =
          scanResult.getClassesImplementing(EntityRenderer.class.getName());

      List<EntityRenderer> renderers =
          rendererClasses.loadClasses(EntityRenderer.class).stream()
              .map(
                  cls -> {
                    try {
                      return cls.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                      throw new RuntimeException(
                          "Failed to instantiate renderer: " + cls.getName(), e);
                    }
                  })
              .sorted(Comparator.comparing(r -> r.getClass().equals(DefaultRenderer.class)))
              .toList();

      System.out.println(
          "Discovered and loaded " + renderers.size() + " renderers via ClassGraph.");
      return renderers;
    }
  }
}
