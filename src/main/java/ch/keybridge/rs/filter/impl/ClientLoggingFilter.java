/*
 * Copyright 2015 Caulfield IP Holdings (Caulfield) and affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * Software Code is protected by copyright. Caulfield hereby
 * reserves all rights and copyrights and no license is
 * granted under said copyrights in this Software License Agreement.
 * Caulfield generally licenses software for commercialization
 * pursuant to the terms of either a Standard Software Source Code
 * License Agreement or a Standard Product License Agreement.
 * A copy of these agreements may be obtained by sending a request
 * via email to info@caufield.org.
 */
package ch.keybridge.rs.filter.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * Client logging filter based on JavaEE 7 example.
 * <p>
 * Use the register(...) method to explicitly specify the collection of the
 * provider contracts for which the JAX-RS component should be registered and/or
 * the priority of each registered provider contract.
 *
 * @author Key Bridge LLC
 * @see
 * <a href="https://jax-rs-spec.java.net/nonav/2.0/apidocs/javax/ws/rs/core/Configurable.html">Configurable</a>
 * @since 1.2.0 snapshot created 11/04/15
 */
public class ClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

  /**
   * Find or create a logger for a named subsystem. If a logger has already been
   * created with the given name it is returned. Otherwise a new logger is
   * created.
   */
  private static final Logger LOG = Logger.getLogger(ClientLoggingFilter.class.getName());

  private long startTime;

  public ClientLoggingFilter() {
    this.startTime = System.currentTimeMillis();
  }

  /**
   * Query logging filter. Log the QUERY before sent to the server.
   *
   * @param request the request context
   * @throws IOException if either context cannot be read
   */
  @Override
  public void filter(ClientRequestContext request) throws IOException {
    /**
     * Start a timer. The elapsed time is logged in the response filter.
     */
    this.startTime = System.currentTimeMillis();
  }

  /**
   * Response logging filter. Log the RESPONSE received from the server.
   *
   * @param request  the request context
   * @param response the response context
   * @throws IOException if either context cannot be read
   */
  @Override
  public void filter(ClientRequestContext request, ClientResponseContext response) throws IOException {
    /**
     * If the response includes an ERROR message (from a Key Bridge web service)
     * then log a WARNING with the ERROR message. Otherwise just log the
     * transaction and time.
     */
    long duration = System.currentTimeMillis() - startTime;

    if (response.getHeaderString("ERROR") != null) {
      LOG.log(Level.WARNING, "Client HTTP QUERY  {0}: {1}  Error: {2}  Time: {3}", new Object[]{request.getMethod(), request.getUri(), response.getHeaderString("ERROR"), duration});
    } else {
      LOG.log(Level.INFO, "Client HTTP QUERY  {0}: {1}  Time: {2}", new Object[]{request.getMethod(), request.getUri(), duration});
    }
  }
}
