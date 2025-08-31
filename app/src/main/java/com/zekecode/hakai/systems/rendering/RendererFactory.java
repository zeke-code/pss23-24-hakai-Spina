package com.zekecode.hakai.systems.rendering;

import com.zekecode.hakai.systems.rendering.renderers.DefaultRenderer;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.Comparator;
import java.util.List;

public class RendererFactory {

  public static List<EntityRenderer> createRenderers() {
    String rendererPackage = "com.zekecode.hakai.systems.rendering.renderers";

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
