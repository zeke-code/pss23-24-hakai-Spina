package com.zekecode.hakai.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import com.zekecode.hakai.engine.data.PowerUpData;
import com.zekecode.hakai.entities.EntityFactory;
import com.zekecode.hakai.powerups.PowerUpTrigger;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LevelManagerTest {

  @Mock private EntityFactory entityFactory;
  @Captor private ArgumentCaptor<Double> xCaptor, yCaptor, widthCaptor, heightCaptor;
  @Captor private ArgumentCaptor<Color> colorCaptor;
  @Captor private ArgumentCaptor<Integer> hpCaptor;
  @Captor private ArgumentCaptor<PowerUpData> powerUpCaptor;

  private LevelManager levelManager;

  @BeforeEach
  void setUp() {
    // The LevelManager needs a real LevelLoader, but a mocked EntityFactory
    levelManager = new LevelManager(entityFactory);
  }

  @Test
  void loadAndBuildLevel_shouldCreateCorrectBricksFromYaml() {
    // ACT
    levelManager.loadAndBuildLevel("test_level.yml");

    // ASSERT
    // Our test_level.yml has two bricks ('R' and 'B'), so createBrick should be called twice.
    verify(entityFactory, times(2))
        .createBrick(
            xCaptor.capture(),
            yCaptor.capture(),
            widthCaptor.capture(),
            heightCaptor.capture(),
            colorCaptor.capture(),
            hpCaptor.capture(),
            powerUpCaptor.capture());

    // --- Verify the first brick ('R') ---
    // Position: row 0, col 0. x = padding = 5. y = offsetTop = 50.
    assertEquals(5.0, xCaptor.getAllValues().get(0));
    assertEquals(50.0, yCaptor.getAllValues().get(0));
    assertEquals(Color.web("#FF0000"), colorCaptor.getAllValues().get(0));
    assertEquals(1, hpCaptor.getAllValues().get(0));
    PowerUpData firstPowerUp = powerUpCaptor.getAllValues().get(0);
    assertEquals("PADDLE_EXPAND", firstPowerUp.type);
    assertEquals(PowerUpTrigger.ON_COLLECT, firstPowerUp.trigger);

    // --- Verify the second brick ('B') ---
    // Position: row 1, col 1.
    // x = (col * (width + padding)) + padding = (1 * (60 + 5)) + 5 = 70
    // y = (row * (height + padding)) + offsetTop = (1 * (20 + 5)) + 50 = 75
    assertEquals(70.0, xCaptor.getAllValues().get(1));
    assertEquals(75.0, yCaptor.getAllValues().get(1));
    assertEquals(Color.web("#0000FF"), colorCaptor.getAllValues().get(1));
    assertEquals(2, hpCaptor.getAllValues().get(1));
    assertNull(powerUpCaptor.getAllValues().get(1), "Brick 'B' should have no power-up.");
  }
}
