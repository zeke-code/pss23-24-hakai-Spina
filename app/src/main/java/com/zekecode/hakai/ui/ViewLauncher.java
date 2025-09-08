package com.zekecode.hakai.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point for launching the JavaFX application. This class initializes the primary
 * stage and displays the main menu. I added this class simply to separate the JavaFX launch logic
 * from the rest of the application. Without it, you get an error about missing modules required to
 * run your application.
 */
public class ViewLauncher extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    SceneManager sceneManager = new SceneManager(primaryStage);
    sceneManager.showMainMenu();
    sceneManager.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
