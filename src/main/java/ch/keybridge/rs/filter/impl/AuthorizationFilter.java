/*
 * Copyright 2020 Key Bridge. All rights reserved. Use is subject to license
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
package ch.keybridge.rs.filter.impl;

import ch.keybridge.rs.AuthorizationValidator;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

/**
 * An HTTP authorization request filter. Evaluates the credentials to
 * authenticate a user agent with a server, usually, but not necessarily, after
 * the server has responded with a 401 Unauthorized status and the
 * WWW-Authenticate header.
 *
 * @author Key Bridge
 * @since v0.7.0 created 2020-08-12
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization">HTTP
 * Authorization</a>
 */
@Priority(Priorities.AUTHORIZATION) // Security authorization filter/interceptor priority.
public class AuthorizationFilter implements ContainerRequestFilter {

  /**
   * The authorization validator implementation.
   */
  private final AuthorizationValidator validator;

  /**
   * Construct a new filter using the provided validator.
   *
   * @param validator an http authorization validator instance.
   */
  public AuthorizationFilter(AuthorizationValidator validator) {
    this.validator = validator;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Evaluate the HTTP Authorization request header to validate the request. If
   * validated then the security context is set identifying the current user.
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    /**
     * Get the authorization header value. Syntax is
     * {@code Authorization: <type> <credentials>}. Returns the
     * {@code <type> <credentials>} payload.
     */
    String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    if (authorization == null || authorization.trim().isEmpty()) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    /**
     * Capture the authorization type and credentials.
     */
    String type = authorization.split(" ")[0];
    String credentials = new String(parseBase64Binary(authorization.replaceFirst(type, "")));
    /**
     * Try to validate the authentication credentials.
     */
    SecurityContext securityContext = validator.validate(type, credentials); // throws WebApplicationException
    if (securityContext == null) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    /**
     * Set the security context.
     */
    requestContext.setSecurityContext(securityContext);
  }

}
