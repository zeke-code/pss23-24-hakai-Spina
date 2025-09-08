package com.zekecode.hakai.systems;

import static org.mockito.Mockito.*;

import com.google.common.eventbus.EventBus;
import com.zekecode.hakai.components.entities.BrickComponent;
import com.zekecode.hakai.core.Entity;
import com.zekecode.hakai.core.World;
import com.zekecode.hakai.events.LevelClearEvent;
import com.zekecode.hakai.events.brick.BrickDestroyedEvent;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LevelCompletionSystemTest {

    @Mock private World world;
    @Mock private EventBus eventBus;

    private LevelCompletionSystem system;
    private Entity brick1;
    private Entity brick2;

    @BeforeEach
    void setUp() {
        system = new LevelCompletionSystem(world, eventBus);
        brick1 = new Entity(1);
        brick1.addComponent(new BrickComponent(1));
        brick2 = new Entity(2);
        brick2.addComponent(new BrickComponent(1));
    }

    @Test
    void onBrickDestroyed_whenBricksRemain_shouldNotPostLevelClearEvent() {
        // ARRANGE: Simulate a world where two bricks still exist
        when(world.getEntities()).thenReturn(List.of(brick1, brick2));
        BrickDestroyedEvent event = new BrickDestroyedEvent(brick1);

        // ACT
        system.onBrickDestroyed(event);

        // ASSERT: The system should not post the level clear event
        verify(eventBus, never()).post(any(LevelClearEvent.class));
    }

    @Test
    void onBrickDestroyed_whenItIsTheLastBrick_shouldPostLevelClearEvent() {
        // ARRANGE: Simulate a world where only one brick is left.
        // The check happens after the brick is marked dead, but before it's removed,
        // so the count of remaining bricks will be 1.
        when(world.getEntities()).thenReturn(List.of(brick1));
        BrickDestroyedEvent event = new BrickDestroyedEvent(brick1);

        // ACT
        system.onBrickDestroyed(event);

        // ASSERT: The system MUST post the level clear event
        verify(eventBus, times(1)).post(any(LevelClearEvent.class));
    }
}