package com.zekecode.hakai.ui.menus;

import com.zekecode.hakai.ui.SceneManager;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.yaml.snakeyaml.Yaml;

/**
 * A factory class for creating the UI panes for various game menus. This keeps UI creation logic
 * separate from the main application class.
 */
public class MenuFactory {

  public static Pane createMainMenu(SceneManager sceneManager) {
    VBox buttons = new VBox(20);
    buttons.setAlignment(Pos.CENTER);

    Button playButton = createMenuButton("Play");
    playButton.setOnAction(e -> sceneManager.showLevelSelect());

    Button howToPlayButton = createMenuButton("How to Play");
    howToPlayButton.setOnAction(e -> sceneManager.showHowToPlay());

    Button exitButton = createMenuButton("Exit");
    exitButton.setOnAction(e -> System.exit(0));

    buttons.getChildren().addAll(playButton, howToPlayButton, exitButton);

    Text title = new Text("Hakai");
    title.getStyleClass().add("menu-title"); // Use the big title style

    return createMenuLayout(title, buttons);
  }

  public static Pane createLevelSelectMenu(SceneManager sceneManager) {
    VBox levelButtons = new VBox(15);
    levelButtons.setAlignment(Pos.CENTER);
    levelButtons.setPadding(new Insets(20));
    Yaml yaml = new Yaml();

    try (ScanResult scanResult = new ClassGraph().acceptPaths("levels/").scan()) {
      List<Resource> levelFiles = scanResult.getResourcesWithExtension("yml");
      Collections.sort(levelFiles);

      for (Resource levelResource : levelFiles) {
        String path = levelResource.getPath();
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        String fallbackName = fileName.replace(".yml", "").replace("_", " ").toUpperCase();
        String levelName = fallbackName;

        try (InputStream inputStream = levelResource.open()) {
          Map<String, Object> data = yaml.load(inputStream);
          if (data != null && data.containsKey("levelName")) {
            levelName = (String) data.get("levelName");
          }
        } catch (Exception e) {
          System.err.println(
              "Warning: Could not parse " + fileName + ". Using filename as fallback.");
        }

        Button levelButton = createMenuButton(levelName);
        levelButton.setOnAction(e -> sceneManager.startGame(fileName));
        levelButtons.getChildren().add(levelButton);
      }
    }

    Button backButton = createMenuButton("Back");
    backButton.setOnAction(e -> sceneManager.showMainMenu());
    levelButtons.getChildren().add(backButton);

    Text title = new Text("Select Level");
    title.getStyleClass().add("menu-subtitle"); // Use the smaller subtitle style

    return createMenuLayout(title, levelButtons);
  }

  public static Pane createHowToPlayMenu(SceneManager sceneManager) {
    VBox content = new VBox(20);
    content.setAlignment(Pos.CENTER);

    Text instructionsTitle = new Text("How to Play");
    instructionsTitle.getStyleClass().add("menu-subtitle");

    Text controls =
        new Text(
            """
                                    Use the LEFT/RIGHT arrow keys or A/D to move the paddle.
                                    Press UP arrow or W to launch the ball.
                                    Press P to pause the game at any time.
                                    Press ESC to return to the Main Menu.

                                    Clear all the bricks to win the level!""");
    controls.getStyleClass().add("menu-text");
    controls.setWrappingWidth(700);

    Button backButton = createMenuButton("Back");
    backButton.setOnAction(e -> sceneManager.showMainMenu());

    content.getChildren().addAll(instructionsTitle, controls, backButton);

    Text title = new Text("Instructions");
    title.getStyleClass().add("menu-subtitle"); // Use the smaller subtitle style

    return createMenuLayout(title, content);
  }

  private static Button createMenuButton(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("menu-button");
    return button;
  }

  private static BorderPane createMenuLayout(Text title, javafx.scene.Node content) {
    BorderPane layout = new BorderPane();
    layout.getStyleClass().add("menu-pane");

    BorderPane.setAlignment(title, Pos.CENTER);
    BorderPane.setMargin(title, new Insets(50, 0, 0, 0));

    layout.setTop(title);
    layout.setCenter(content);
    return layout;
  }
}
