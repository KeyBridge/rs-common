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

import ch.keybridge.rs.type.AuthorizationType;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * An abstract HTTP authentication (AuthN) request filter. Extracts the
 * 'Authentication' header to extract the type and credentials. Implementations
 * extending this class must validate the credentials and set the security
 * context.
 * <p>
 * Evaluates the credentials to authenticate a user agent with a server,
 * usually, but not necessarily, after the server has responded with a 401
 * Unauthorized status and the WWW-Authenticate header.
 * <p>
 * The HTTP `Authorization` request header contains the credentials to
 * authenticate a user agent with a server, usually, but not necessarily, after
 * the server has responded with a 401 Unauthorized status and the
 * WWW-Authenticate header.
 * <p>
 * Header syntax is {@code Authorization: <type> <credentials>}
 * <p>
 * Use instructions: Extend this class, annotate the instance with: <br>
 * Provider <br>
 * Dependent <br>
 * HttpAuthorization <br>
 * Priority(Priorities.AUTHENTICATION) <br>
 *
 * @author Key Bridge
 * @since v0.7.0 created 2020-08-12
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization">HTTP
 * Authorization</a>
 * @see
 * <a href="http://www.iana.org/assignments/http-authschemes/http-authschemes.xhtml">HTTP
 * Authentication Scheme Registry</a>
 * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.2">HTTP
 * Authentication</a>
 * @see <a href="https://tools.ietf.org/html/rfc7617">Basic</a>
 * @see <a href="https://tools.ietf.org/html/rfc6750">Oauth bearer profile</a>
 * @see <a href="https://tools.ietf.org/html/rfc7523">JWT bearer profile</a>
 * @see <a href="https://tools.ietf.org/html/rfc7616">Digest</a>
 * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.1">Oauth</a>
 * @see <a href="https://tools.ietf.org/html/rfc7486">HOBA</a>
 * @see <a href="https://tools.ietf.org/html/rfc8120">Mutual</a>
 * @see <a href="https://tools.ietf.org/html/rfc4559#section-4">Negotiate</a>
 * @see <a href="https://tools.ietf.org/html/rfc5802">SCRAM</a>
 * @see <a href="https://tools.ietf.org/html/rfc8292">Vapid</a>
 */
//@Provider
//@Dependent
//@HttpAuthorization
//@Priority(Priorities.AUTHENTICATION) // Security authentication filter/interceptor priority.
public abstract class AbstractHttpAuthenticationFilter implements ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(AbstractHttpAuthenticationFilter.class.getName());

  /**
   * The Container request filter context for the instant request. Provides
   * request-specific information for the filter, such as request URI, message
   * headers, message entity or request-scoped properties. The exposed setters
   * allow modification of the exposed request-specific information.
   */
  protected ContainerRequestContext requestContext;
  /**
   * The request header authorization type.
   */
  protected AuthorizationType authorizationType;

  /**
   * {@inheritDoc}
   * <p>
   * Evaluate the HTTP Authorization request header and validate the request. If
   * validated then the security context is set identifying the current user.
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    /**
     * Get the authorization header value. Syntax is
     * {@code Authorization: <type> <credentials>}. Returns the
     * {@code <type> <credentials>} payload.
     */
    String authorizationPayload = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    if (authorizationPayload == null || authorizationPayload.trim().isEmpty()) {
      LOG.info("Invalid or missing " + HttpHeaders.AUTHORIZATION + " header");
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    /**
     * Capture the authorization type and credentials.
     */
    String type = authorizationPayload.split("\\s")[0];
    String credentials = authorizationPayload.replaceFirst(type, "").trim();
    /**
     * Try to validate the authorization type.
     */
    authorizationType = AuthorizationType.parse(type);
    if (authorizationType == null) {
      LOG.log(Level.INFO, "Unrecognized http authorization '{'type={0}'}'", type);
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    /**
     * Provide a reference to the request context in case it is needed by the
     * filter implementation.
     */
    this.requestContext = requestContext;
    /**
     * Try to validate the authentication credentials. Copy the
     * requestContext::SecurityContext::secure status.
     */
    SecurityContext securityContext = this.validate(credentials); // throws WebApplicationException
    if (securityContext == null) {
      LOG.log(Level.WARNING, "{0} validate() method returned null security context.", this.getClass().getSimpleName());
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    /**
     * Set the security context.
     */
    requestContext.setSecurityContext(securityContext);
  }

  /**
   * Validate the authorization. Produces a SecurityContext implementation that
   * provides access to security related information for the indicated
   * authorization detail. Note that the validate implementation MUST be
   * compatible with the `authorizationType`.
   *
   * @param credentials the authentication credential. This typically encodes an
   *                    access key ID and scope information.
   * @return the SecurityContext implementation for the user corresponding to
   *         the provided credentials
   */
  public abstract SecurityContext validate(String credentials);

}
