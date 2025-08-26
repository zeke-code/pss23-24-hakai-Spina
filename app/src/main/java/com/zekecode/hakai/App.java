package com.zekecode.hakai;

import com.zekecode.hakai.core.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  private World world;

  // TODO: move all setup logic to a game manager or initializer class. Keep entrypoint clean.
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Hakai");

    Pane root = new Pane();
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    root.getChildren().add(canvas);

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();

    world = new World();

    // Game Loop
    AnimationTimer gameLoop =
        new AnimationTimer() {
          private long lastUpdate = 0;

          @Override
          public void handle(long now) {
            if (lastUpdate == 0) {
              lastUpdate = now;
              return;
            }

            double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

            world.update(deltaTime);

            lastUpdate = now;
          }
        };
    gameLoop.start();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
