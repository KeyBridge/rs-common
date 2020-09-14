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
import java.util.Collection;
import javax.ws.rs.core.SecurityContext;

/**
 * Abstract HTTP bearer {@link SecurityContext} implementation for opaque Token
 * based bearer authentication. An opaque token reveals no details other than
 * the value itself (like a random string).
 *
 * @author Key Bridge
 * @since v0.8.0 created 2020-08-12
 */
public abstract class AbstractTokenSecurityContext implements SecurityContext {

  /**
   * The authentication scheme used to protect the resource
   */
  protected final AuthorizationType scheme = AuthorizationType.BEARER;
  /**
   * boolean indicating whether this request was made using a secure channel,
   * such as HTTPS.
   */
  protected final boolean secure;

  /**
   * A Principal object containing the name of the current authenticated user.
   */
  private final Principal principal;

  /**
   * The list of scopes associated with the current user;
   */
  protected final Collection<String> scope;

  /**
   * Default, fully qualified constructor.
   *
   * @param principal A Principal object containing the name of the current
   *                  authenticated user.
   * @param scope     The list of scopes associated with the current user;
   * @param secure    Whether this request was made using a secure channel, such
   *                  as HTTPS.
   */
  public AbstractTokenSecurityContext(Principal principal, Collection<String> scope, boolean secure) {
    this.principal = principal;
    this.scope = scope;
    this.secure = secure;
  }

  /**
   * {@inheritDoc} The current authenticated user as identified by the token
   * instance.
   */
  @Override
  public Principal getUserPrincipal() {
    return principal;
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
