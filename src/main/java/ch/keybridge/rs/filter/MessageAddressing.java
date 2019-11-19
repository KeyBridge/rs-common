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
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Name binding annotation to bind a provider using the @MessageAddressing
 * annotation, which will enable the Web Service Addressing filter.
 * <p>
 * Web Services Addressing provides transport-neutral mechanisms to address Web
 * services and messages. Web Services Addressing 1.0 - Core (WS-Addressing)
 * defines two constructs, message addressing properties and endpoint
 * references, that normalize the information typically provided by transport
 * protocols and messaging systems in a way that is independent of any
 * particular transport or messaging system.
 * <p>
 * Implementations of this filter MUST support, at minimum, the MessageId and
 * RelatesTo fields.
 * <p>
 * Message addressing properties collectively augment a message with the
 * following abstract properties to support one-way, request-response, and other
 * interaction patterns
 *
 * @author Key Bridge
 * @see <a href="https://www.w3.org/TR/ws-addr-core/">Web Services
 * Addressing</a>
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MessageAddressing {

}
