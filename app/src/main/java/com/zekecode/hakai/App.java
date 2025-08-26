package com.zekecode.hakai;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Modern Breakout");
    primaryStage.show();
    // TODO: setup game loop and initialize game state here, later to be moved in a GameManager
    // class
  }
}
