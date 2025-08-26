package com.zekecode.hakai;

import com.zekecode.hakai.engine.GameLoop;
import com.zekecode.hakai.engine.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Hakai");
    Pane root = new Pane();
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    root.getChildren().add(canvas);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);

    GraphicsContext gc = canvas.getGraphicsContext2D();
    GameManager gameManager = new GameManager(gc, WIDTH, HEIGHT);
    GameLoop gameLoop = new GameLoop(gameManager);

    scene.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.P) {
            gameManager.togglePause();
          }
          // We will add more input handling here later
        });

    gameLoop.start();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
