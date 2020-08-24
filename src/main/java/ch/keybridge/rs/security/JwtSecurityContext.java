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
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.ws.rs.core.SecurityContext;

/**
 * HTTP BEARER {@link SecurityContext} implementation for JWT (Json-Web-Token)
 * based authentication. Holds details about the authentication token. A JWT
 * token is self-contained and includes claims to identify the user, scope, etc.
 * JWT enables stateless authentication.
 * <p>
 * The authentication scheme is `Bearer`.
 *
 * @author Key Bridge
 * @since v0.8.0 created 2020-08-24
 */
public class JwtSecurityContext implements SecurityContext {

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
  private final Collection<String> scope;

  /**
   * The token identifier. Equivalent to a key id.
   */
  private final String jti;
  private final ZonedDateTime notBefore;
  private final ZonedDateTime notAfter;
  private final int refreshCount;
  private final int refreshLimit;

  public JwtSecurityContext(boolean secure, String userId, Collection<String> scope, String jti, ZonedDateTime notBefore, ZonedDateTime notAfter, int refreshCount, int refreshLimit) {
    this.secure = secure;
    this.userId = userId;
    this.scope = scope;
    this.jti = jti;
    this.notBefore = notBefore;
    this.notAfter = notAfter;
    this.refreshCount = refreshCount;
    this.refreshLimit = refreshLimit;
  }

  //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
  public AuthorizationType getScheme() {
    return scheme;
  }

  public String getUserId() {
    return userId;
  }

  public Collection<String> getScope() {
    return scope;
  }

  public String getJti() {
    return jti;
  }

  public ZonedDateTime getNotBefore() {
    return notBefore;
  }

  public ZonedDateTime getNotAfter() {
    return notAfter;
  }

  public int getRefreshCount() {
    return refreshCount;
  }

  public int getRefreshLimit() {
    return refreshLimit;
  }//</editor-fold>

  /**
   * Check if the authentication token is eligible for refreshment.
   *
   * @return TRUE if the authentication token is eligible for refreshment
   */
  public boolean isEligibleForRefreshment() {
    return refreshCount < refreshLimit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSecure() {
    return secure;
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
  public String getAuthenticationScheme() {
    return scheme.name();
  }

  /**
   * Security context builder class. Helps to assemble the security context
   * using fluent setter methods.
   */
  public static class Builder {

    private boolean secure;
    private String userId;
    private Collection<String> scope;
    private String jti;
    private ZonedDateTime notBefore;
    private ZonedDateTime notAfter;
    private int refreshCount;
    private int refreshLimit;

    public Builder withSecure(boolean secure) {
      this.secure = secure;
      return this;
    }

    public Builder withUserId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder withScope(Collection<String> scope) {
      this.scope = Collections.unmodifiableCollection(scope == null ? new HashSet<>() : scope);
      return this;
    }

    public Builder withJti(String jti) {
      this.jti = jti;
      return this;
    }

    public Builder withNotBefore(ZonedDateTime notBefore) {
      this.notBefore = notBefore;
      return this;
    }

    public Builder withNotAfter(ZonedDateTime notAfter) {
      this.notAfter = notAfter;
      return this;
    }

    public Builder withRefreshCount(int refreshCount) {
      this.refreshCount = refreshCount;
      return this;
    }

    public Builder withRefreshLimit(int refreshLimit) {
      this.refreshLimit = refreshLimit;
      return this;
    }

    /**
     * Build a complete JwtSecurityContext instance.
     *
     * @return a complete JwtSecurityContext instance
     */
    public JwtSecurityContext build() {
      if (userId == null) {
        throw new IllegalArgumentException("userId is required");
      }
      if (scope == null) {
        throw new IllegalArgumentException("scope is required");
      }
      if (notBefore == null) {
        throw new IllegalArgumentException("notBefore is required");
      }
      if (notAfter == null) {
        throw new IllegalArgumentException("notAfter is required");
      }
      if (jti == null) {
        throw new IllegalArgumentException("jti is required");
      }
      return new JwtSecurityContext(secure, userId, scope, jti, notBefore, notAfter, refreshCount, refreshLimit);
    }

  }

}
