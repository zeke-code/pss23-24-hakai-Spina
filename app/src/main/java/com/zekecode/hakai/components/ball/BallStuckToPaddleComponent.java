package com.zekecode.hakai.components.ball;

import com.zekecode.hakai.core.Component;

/**
 * Plain component that acts as a state to signal whether our ball needs to stick to the paddle. I
 * didn't want to create it as a boolean flag in the BallComponent as checking that flag would
 * require a lot of computation.
 *
 * <p>Checking for this component is way faster.
 */
public class BallStuckToPaddleComponent implements Component {}
