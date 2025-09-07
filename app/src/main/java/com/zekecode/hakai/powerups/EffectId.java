package com.zekecode.hakai.powerups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation used to associate a unique string identifier with a concrete {@link Effect}
 * implementation. The {@link EffectRegistry} uses this ID to discover and manage effects.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EffectId {
  String value();
}
