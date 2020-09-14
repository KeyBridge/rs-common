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

import java.security.Principal;
import java.time.ZonedDateTime;

/**
 * A JSON Web Token (JWT) security context principal entity. This is used to
 * record user information referenced by a JWT.
 *
 * @author Key Bridge
 * @since v0.9.0 created 2020-09-05
 */
public class JwtPrincipal implements Principal {

  /**
   * 4.1.1. "iss" (Issuer) Claim The "iss" (issuer) claim identifies the
   * principal that issued the JWT. The processing of this claim is generally
   * application specific. The "iss" value is a case-sensitive string containing
   * a StringOrURI value. Use of this claim is OPTIONAL.
   */
  private String issuer;
  /**
   * 4.1.2. "sub" (Subject) Claim The "sub" (subject) claim identifies the
   * principal that is the subject of the JWT. The claims in a JWT are normally
   * statements about the subject. The subject value MUST either be scoped to be
   * locally unique in the context of the issuer or be globally unique. The
   * processing of this claim is generally application specific. The "sub" value
   * is a case-sensitive string containing a StringOrURI value. Use of this
   * claim is OPTIONAL.
   */
  private String subject;
  /**
   * 4.1.3. "aud" (Audience) Claim The "aud" (audience) claim identifies the
   * recipients that the JWT is intended for. Each principal intended to process
   * the JWT MUST identify itself with a value in the audience claim. If the
   * principal processing the claim does not identify itself with a value in the
   * "aud" claim when this claim is present, then the JWT MUST be rejected. In
   * the general case, the "aud" value is an array of case- sensitive strings,
   * each containing a StringOrURI value. In the special case when the JWT has
   * one audience, the "aud" value MAY be a single case-sensitive string
   * containing a StringOrURI value. The interpretation of audience values is
   * generally application specific. Use of this claim is OPTIONAL.
   */
  private String audience;
  /**
   * 4.1.4. "exp" (Expiration Time) Claim The "exp" (expiration time) claim
   * identifies the expiration time on or after which the JWT MUST NOT be
   * accepted for processing. The processing of the "exp" claim requires that
   * the current date/time MUST be before the expiration date/time listed in the
   * "exp" claim. Implementers MAY provide for some small leeway, usually no
   * more than a few minutes, to account for clock skew. Its value MUST be a
   * number containing a NumericDate value. Use of this claim is OPTIONAL.
   */
  private ZonedDateTime expirationTime;
  /**
   * 4.1.5. "nbf" (Not Before) Claim The "nbf" (not before) claim identifies the
   * time before which the JWT MUST NOT be accepted for processing. The
   * processing of the "nbf" claim requires that the current date/time MUST be
   * after or equal to the not-before date/time listed in the "nbf" claim.
   * Implementers MAY provide for some small leeway, usually no more than a few
   * minutes, to account for clock skew. Its value MUST be a number containing a
   * NumericDate value. Use of this claim is OPTIONAL.
   */
  private ZonedDateTime notBefore;
  /**
   * 4.1.6. "iat" (Issued At) Claim The "iat" (issued at) claim identifies the
   * time at which the JWT was issued. This claim can be used to determine the
   * age of the JWT. Its value MUST be a number containing a NumericDate value.
   * Use of this claim is OPTIONAL.
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
  private String jwtId;

  /**
   * Default no-arg constructor.
   */
  public JwtPrincipal() {
  }

  /**
   * Fully qualified constructor.
   *
   * @param issuer         "iss" (Issuer) Claim
   * @param subject        "sub" (Subject) Claim
   * @param audience       "aud" (Audience) Claim
   * @param expirationTime "exp" (Expiration Time) Claim
   * @param notBefore      "nbf" (Not Before) Claim
   * @param issuedAt       "iat" (Issued At) Claim
   * @param jwtId          "jti" (JWT ID) Claim
   */
  public JwtPrincipal(String issuer, String subject, String audience, ZonedDateTime expirationTime, ZonedDateTime notBefore, ZonedDateTime issuedAt, String jwtId) {
    this.issuer = issuer;
    this.subject = subject;
    this.audience = audience;
    this.expirationTime = expirationTime;
    this.notBefore = notBefore;
    this.issuedAt = issuedAt;
    this.jwtId = jwtId;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Returns the JWT ID ("jti") value. The JTI is the token id. Reference the
   * `userId` field to identify the user account to which the JTI is bound.
   */
  @Override
  public String getName() {
    return jwtId;
  }

  //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getAudience() {
    return audience;
  }

  public void setAudience(String audience) {
    this.audience = audience;
  }

  public ZonedDateTime getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(ZonedDateTime expirationTime) {
    this.expirationTime = expirationTime;
  }

  public ZonedDateTime getNotBefore() {
    return notBefore;
  }

  public void setNotBefore(ZonedDateTime notBefore) {
    this.notBefore = notBefore;
  }

  public ZonedDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(ZonedDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getJwtId() {
    return jwtId;
  }

  public void setJwtId(String jwtId) {
    this.jwtId = jwtId;
  }//</editor-fold>

}
