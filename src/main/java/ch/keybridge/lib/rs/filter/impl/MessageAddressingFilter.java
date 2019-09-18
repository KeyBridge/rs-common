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
package ch.keybridge.lib.rs.filter.impl;

import ch.keybridge.lib.rs.filter.MessageAddressing;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

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
 * Note: IRI is an Internationalized Resource Identifiers (IRIs) defined in
 * RFC3987, which is just a URI (ASCII) but encoded using UTF-8.
 *
 * @author Key Bridge
 * @see <a href="https://www.w3.org/TR/ws-addr-core/">Web Services
 * Addressing</a>
 * @see
 * <a href="https://www.w3.org/TR/ws-addr-core/#msgaddrpropsinfoset/">Message
 * Addressing Properties</a>
 * @since v0.25.0 created 12/18/18
 */
@Provider
@MessageAddressing
@Priority(Priorities.HEADER_DECORATOR)  // Header decorator filter/interceptor
public class MessageAddressingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger LOG = Logger.getLogger(MessageAddressingFilter.class.getName());

  /**
   * An absolute IRI that uniquely identifies the message. When present, it is
   * the responsibility of the sender to ensure that each message is uniquely
   * identified. The behavior of a receiver when receiving a message that
   * contains the same [message id] as a previously received message is
   * unconstrained by this specification.
   */
  private static final String MESSAGE_ID = "MessageID";
  /**
   * Conveys the relationship type as an IRI. When absent, the implied value of
   * this attribute is "http://www.w3.org/2005/08/addressing/reply". This
   * OPTIONAL (repeating) element information item contributes one abstract
   * [relationship] property value, in the form of an (IRI, IRI) pair. The
   * content of this element (of type xs:anyURI) conveys the [message id] of the
   * related message.
   */
  private static final String RELATES_TO = "RelatesTo";
  /**
   * An absolute IRI that uniquely identifies the semantics implied by this
   * message. It is RECOMMENDED that the value of the [action] property is an
   * IRI identifying an input, output, or fault message within a WSDL interface
   * or port type. An action may be explicitly or implicitly associated with the
   * corresponding WSDL definition. Web Services Addressing 1.0 - WSDL
   * Binding[WS-Addressing WSDL Binding] describes the mechanisms of
   * association.
   * <p>
   * Key Bridge: This identifies the object type when encrypted. e.g.
   * {@code http://opencbrs.org/ws/peer/escService/pingResponse}
   */
  private static final String ACTION = "Action";

  /**
   * {@inheritDoc}
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    /**
     * Conditionally add an inbound MessageId header if none is present.
     */
//    if (requestContext.getHeaderString(MESSAGE_ID) == null) {
//      LOG.fine("MessageAddressingFilter add missing MessageID header.");
//      requestContext.getHeaders().add(MESSAGE_ID, UUID.randomUUID().toString());
//    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Add Web Services Addressing response headers.
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    /**
     * Add a unique outbound MessageId header to the response.
     */
    responseContext.getHeaders().add(MESSAGE_ID, UUID.randomUUID().toString());
    /**
     * If the incoming message is identified then add a relatesTo response
     * header.
     */
    String messageId = requestContext.getHeaderString(MESSAGE_ID);
    if (messageId != null) {
      responseContext.getHeaders().add(RELATES_TO, messageId);
    }
  }

}
