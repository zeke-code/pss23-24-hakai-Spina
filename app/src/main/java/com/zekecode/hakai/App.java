package com.zekecode.hakai;

import com.zekecode.hakai.engine.GameLoop;
import com.zekecode.hakai.engine.GameManager;
import com.zekecode.hakai.engine.UIManager;
import com.zekecode.hakai.engine.input.InputHandler;
import com.zekecode.hakai.engine.input.InputManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    InputManager inputManager = new InputManager();
    GameManager gameManager = new GameManager();
    UIManager uiManager = new UIManager(WIDTH, HEIGHT);
    InputHandler inputHandler = new InputHandler(inputManager, gameManager);

    gameManager.setup(gc, inputManager, WIDTH, HEIGHT);
    inputHandler.attach(scene);

    GameLoop gameLoop = new GameLoop(gameManager, uiManager, gc);
    gameLoop.start();

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
