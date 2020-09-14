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

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.ws.rs.core.SecurityContext;

/**
 * The OAuth 2.0 Authorization Framework: Bearer JSON Web Token (JWT) Profile
 * for OAuth 2.0 Client Authentication and Authorization Grants.
 * <p>
 * HTTP BEARER {@link SecurityContext} implementation for JWT (Json-Web-Token)
 * based authentication. Holds details about the authentication token. A JWT
 * token is self-contained and includes claims to identify the user, scope, etc.
 * JWT enables stateless authentication.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7523">JWT bearer profile</a>
 * @author Key Bridge
 * @since v0.8.0 created 2020-08-24
 */
public class JwtSecurityContext extends AbstractTokenSecurityContext {

  /**
   * Construct a new JWT security context. The current authenticated user is
   * identified by the JWT ID ("jti") value, which references the user's API
   * authorization key.
   *
   * @param principal A JwtPrincipal instance containing the conveyed JWT values
   * @param scope     The list of scopes associated with the current user;
   * @param secure    Whether this request was made using a secure channel, such
   *                  as HTTPS.
   */
  public JwtSecurityContext(JwtPrincipal principal, Collection<String> scope, boolean secure) {
    super(principal, scope, secure);
  }

  /**
   * Security context builder class. Helps to assemble the security context
   * using fluent setter methods.
   */
  public static class Builder {

    /**
     * A boolean indicating whether the instant request was made using a secure
     * channel, such as HTTPS.
     */
    private boolean secure;
    /**
     * The token owner's group privileges.
     */
    private Collection<String> scope;  // required

    /**
     * 4.1.1. "iss" (Issuer) Claim The "iss" (issuer) claim identifies the
     * principal that issued the JWT. The processing of this claim is generally
     * application specific. The "iss" value is a case-sensitive string
     * containing a StringOrURI value. Use of this claim is OPTIONAL.
     */
    private String issuer;
    /**
     * 4.1.2. "sub" (Subject) Claim The "sub" (subject) claim identifies the
     * principal that is the subject of the JWT. The claims in a JWT are
     * normally statements about the subject. The subject value MUST either be
     * scoped to be locally unique in the context of the issuer or be globally
     * unique. The processing of this claim is generally application specific.
     * The "sub" value is a case-sensitive string containing a StringOrURI
     * value. Use of this claim is OPTIONAL.
     */
    private String subject; // required
    /**
     * 4.1.3. "aud" (Audience) Claim The "aud" (audience) claim identifies the
     * recipients that the JWT is intended for. Each principal intended to
     * process the JWT MUST identify itself with a value in the audience claim.
     * If the principal processing the claim does not identify itself with a
     * value in the "aud" claim when this claim is present, then the JWT MUST be
     * rejected. In the general case, the "aud" value is an array of case-
     * sensitive strings, each containing a StringOrURI value. In the special
     * case when the JWT has one audience, the "aud" value MAY be a single
     * case-sensitive string containing a StringOrURI value. The interpretation
     * of audience values is generally application specific. Use of this claim
     * is OPTIONAL.
     */
    private String audience;
    /**
     * 4.1.4. "exp" (Expiration Time) Claim The "exp" (expiration time) claim
     * identifies the expiration time on or after which the JWT MUST NOT be
     * accepted for processing. The processing of the "exp" claim requires that
     * the current date/time MUST be before the expiration date/time listed in
     * the "exp" claim. Implementers MAY provide for some small leeway, usually
     * no more than a few minutes, to account for clock skew. Its value MUST be
     * a number containing a NumericDate value. Use of this claim is OPTIONAL.
     */
    private ZonedDateTime expirationTime;
    /**
     * 4.1.5. "nbf" (Not Before) Claim The "nbf" (not before) claim identifies
     * the time before which the JWT MUST NOT be accepted for processing. The
     * processing of the "nbf" claim requires that the current date/time MUST be
     * after or equal to the not-before date/time listed in the "nbf" claim.
     * Implementers MAY provide for some small leeway, usually no more than a
     * few minutes, to account for clock skew. Its value MUST be a number
     * containing a NumericDate value. Use of this claim is OPTIONAL.
     */
    private ZonedDateTime notBefore;  // required
    /**
     * 4.1.6. "iat" (Issued At) Claim The "iat" (issued at) claim identifies the
     * time at which the JWT was issued. This claim can be used to determine the
     * age of the JWT. Its value MUST be a number containing a NumericDate
     * value. Use of this claim is OPTIONAL.
     */
    private ZonedDateTime issuedAt;
    /**
     * 4.1.7. "jti" (JWT ID) Claim The "jti" (JWT ID) claim provides a unique
     * identifier for the JWT. The identifier value MUST be assigned in a manner
     * that ensures that there is a negligible probability that the same value
     * will be accidentally assigned to a different data object; if the
     * application uses multiple issuers, collisions MUST be prevented among
     * values produced by different issuers as well. The "jti" claim can be used
     * to prevent the JWT from being replayed. The "jti" value is a case-
     * sensitive string. Use of this claim is OPTIONAL.
     */
    private String jwtId;  // required

    public Builder withSecure(boolean secure) {
      this.secure = secure;
      return this;
    }

    public Builder withScope(Collection<String> scope) {
      this.scope = Collections.unmodifiableCollection(scope == null ? new HashSet<>() : scope);
      return this;
    }

    public Builder withIssuer(String issuer) {
      this.issuer = issuer;
      return this;
    }

    public Builder withSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder withAudience(String audience) {
      this.audience = audience;
      return this;
    }

    public Builder withExpirationTime(ZonedDateTime expirationTime) {
      this.expirationTime = expirationTime;
      return this;
    }

    public Builder withNotBefore(ZonedDateTime notBefore) {
      this.notBefore = notBefore;
      return this;
    }

    public Builder withIssuedAt(ZonedDateTime issuedAt) {
      this.issuedAt = issuedAt;
      return this;
    }

    public Builder withJwtId(String jwtId) {
      this.jwtId = jwtId;
      return this;
    }

    /**
     * Build a complete JwtSecurityContext instance.
     *
     * @return a complete JwtSecurityContext instance
     */
    public JwtSecurityContext build() {
      if (jwtId == null) {
        throw new IllegalArgumentException("jwtId is required");
      }
      if (scope == null) {
        throw new IllegalArgumentException("scope is required");
      }
      if (notBefore == null) {
        throw new IllegalArgumentException("notBefore is required");
      }
      if (subject == null) {
        throw new IllegalArgumentException("subject is required");
      }

      return new JwtSecurityContext(new JwtPrincipal(issuer, subject, audience, expirationTime, notBefore, issuedAt, jwtId),
                                    scope,
                                    secure);
    }

  }

}
