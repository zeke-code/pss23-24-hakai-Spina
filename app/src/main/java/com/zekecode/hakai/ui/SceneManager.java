package com.zekecode.hakai.ui;

import com.zekecode.hakai.GameBuilder;
import com.zekecode.hakai.config.GameConfig;
import com.zekecode.hakai.engine.game.Game;
import com.zekecode.hakai.ui.menus.MenuFactory;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Manages all scene transitions within the application. This class is responsible for creating and
 * displaying different scenes like the main menu, level selection, and the game itself.
 */
public class SceneManager {

  private final Stage primaryStage;
  private Game currentGame;

  public SceneManager(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Hakai");
    this.primaryStage.setResizable(false);
  }

  /** Displays the main menu screen. */
  public void showMainMenu() {
    if (currentGame != null) {
      currentGame.stop();
      currentGame = null;
    }
    Pane menuRoot = MenuFactory.createMainMenu(this);
    Scene scene = new Scene(menuRoot, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    addStylesheet(scene);
    primaryStage.setScene(scene);
  }

  /** Displays the level selection screen. */
  public void showLevelSelect() {
    Pane levelSelectRoot = MenuFactory.createLevelSelectMenu(this);
    Scene scene = new Scene(levelSelectRoot, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    addStylesheet(scene);
    primaryStage.setScene(scene);
  }

  /** Displays the "How to Play" screen. */
  public void showHowToPlay() {
    Pane howToPlayRoot = MenuFactory.createHowToPlayMenu(this);
    Scene scene = new Scene(howToPlayRoot, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    addStylesheet(scene);
    primaryStage.setScene(scene);
  }

  /**
   * Sets up and starts the main game loop for the selected level.
   *
   * @param levelFile The name of the level file to load (e.g., "level_1.yml").
   */
  public void startGame(String levelFile) {
    Pane root = new Pane();
    Canvas canvas = new Canvas(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    root.getChildren().add(canvas);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    this.currentGame = new GameBuilder().build(gc, scene, levelFile, this);
    this.currentGame.run();
  }

  /** Shows the primary stage. This should be called once from the App's start method. */
  public void show() {
    primaryStage.show();
  }

  private void addStylesheet(Scene scene) {
    URL stylesheet = getClass().getResource("/menu.css");
    if (stylesheet != null) {
      scene.getStylesheets().add(stylesheet.toExternalForm());
    } else {
      System.err.println("Stylesheet not found: /menu.css");
    }
  }
}
