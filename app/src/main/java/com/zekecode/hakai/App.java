package com.zekecode.hakai;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  private Game game; // Keep a reference to the game object

  @Override
  public void start(Stage primaryStage) throws Exception {
    // 1. Setup the JavaFX window
    primaryStage.setTitle("Hakai");
    Pane root = new Pane();
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    root.getChildren().add(canvas);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // 2. Create, initialize, and run the game
    this.game = new Game(gc, scene);
    this.game.initialize();
    this.game.run();

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
