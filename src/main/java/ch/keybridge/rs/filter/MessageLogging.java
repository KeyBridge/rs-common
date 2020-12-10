/*
 * Copyright 2018 Key Bridge. All rights reserved. Use is subject to license
 * terms.
 *
 * This software code is protected by Copyrights and remains the property of
 * Key Bridge and its suppliers, if any. Key Bridge reserves all rights in and to
 * Copyrights and no license is granted under Copyrights in this Software
 * License Agreement.
 *
 * Key Bridge generally licenses Copyrights for commercialization pursuant to
 * the terms of either a Standard Software Source Code License Agreement or a
 * Standard Product License Agreement. A copy of either Agreement can be
 * obtained upon request by sending an email to info@keybridgewireless.com.
 *
 * All information contained herein is the property of Key Bridge and its
 * suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary.
 */
package ch.keybridge.rs.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Name binding annotation to bind a provider using the `@MessageLogging`
 * annotation. Implementations of this filter record, or effect the recording
 * of, HTTP messages to a persistent log.
 * <p>
 * There is a `@NameBinding` annotation applied to the filter, the filter will
 * also be executed at the post-match request extension point, but only in case
 * the matched resource or sub-resource method is bound to the same name-binding
 * annotation.
 *
 * @author Key Bridge
 * @since v0.23.0 added 12/13/18
 * @see
 * <a href="https://blog.dejavu.sk/2014/01/08/binding-jax-rs-providers-to-resource-methods/">Example</a>
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MessageLogging {

}
