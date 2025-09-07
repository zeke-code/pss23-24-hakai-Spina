package com.zekecode.hakai;

import com.zekecode.hakai.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

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
