package com.zekecode.hakai.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/** The main entry point for launching the JavaFX application. */
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
