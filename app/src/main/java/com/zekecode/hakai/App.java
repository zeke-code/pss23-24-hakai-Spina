package com.zekecode.hakai;

import com.zekecode.hakai.engine.Game;
import com.zekecode.hakai.utils.GameBuilder;
import com.zekecode.hakai.utils.GameConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

  private static final int WIDTH = GameConfig.SCREEN_WIDTH;
  private static final int HEIGHT = GameConfig.SCREEN_HEIGHT;

  private Game game;

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

    this.game = new GameBuilder().build(gc, scene);
    this.game.run();

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
