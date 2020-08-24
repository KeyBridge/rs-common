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
package ch.keybridge.rs.security;

import ch.keybridge.rs.type.AuthorizationType;
import java.security.Principal;
import java.util.List;
import javax.ws.rs.core.SecurityContext;

/**
 * HTTP BEARER {@link SecurityContext} implementation for opaque Token based
 * bearer authentication. An opaque token reveals no details other than the
 * value itself (like a random string).
 * <p>
 * The authentication scheme is `Bearer`.
 *
 * @author Key Bridge
 * @since v0.8.0 created 2020-08-12
 */
public class TokenSecurityContext implements SecurityContext {

  /**
   * The authentication scheme used to protect the resource
   */
  private final AuthorizationType scheme = AuthorizationType.BEARER;
  /**
   * boolean indicating whether this request was made using a secure channel,
   * such as HTTPS.
   */
  private final boolean secure;

  /**
   * The current authenticated user. The current authenticated user is
   * identified by the user record UID value or the user's API authorization
   * key.
   */
  private final String userId;
  /**
   * The list of scopes associated with the current user;
   */
  private final List<String> scope;

  /**
   * Default, fully qualified constructor.
   *
   * @param userId The API authorization key of the current authenticated user.
   * @param scope  The list of scopes associated with the current user;
   * @param secure Whether this request was made using a secure channel, such as
   *               HTTPS.
   */
  public TokenSecurityContext(String userId, List<String> scope, boolean secure) {
    this.userId = userId;
    this.scope = scope;
    this.secure = secure;
  }

  /**
   * {@inheritDoc} The current authenticated user is identified by the user
   * record UID value, which may be a user or, more commonly, the user's API
   * authorization key.
   */
  @Override
  public Principal getUserPrincipal() {
    return () -> userId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUserInRole(String role) {
    return scope.contains(role);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSecure() {
    return secure;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthenticationScheme() {
    return scheme.name();
  }

}
