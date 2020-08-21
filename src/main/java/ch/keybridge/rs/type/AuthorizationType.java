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
package ch.keybridge.rs.type;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumerated HTTP Authorization types.
 *
 * @author Key Bridge
 * @since v0.7.0 created 2020-08-12
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization">HTTP
 * Authorization</a>
 */
public enum AuthorizationType {
  /**
   * The 'Basic' HTTP Authentication Scheme
   *
   * @see <a href="https://tools.ietf.org/html/rfc7617">Basic authentication</a>
   */
  BASIC,
  /**
   * The OAuth 2.0 Authorization Framework: Bearer Token Usage or JSON Web Token
   * (JWT) Profile for OAuth 2.0 Client Authentication and Authorization Grants
   *
   * @see <a href="https://tools.ietf.org/html/rfc6750">Oauth bearer profile</a>
   * @see <a href="https://tools.ietf.org/html/rfc7523">JWT bearer profile</a>
   */
  BEARER,
  /**
   * HTTP Digest Access Authentication
   *
   * @see <a href="https://tools.ietf.org/html/rfc7616">Digest
   * authentication</a>
   */
  DIGEST,
  /**
   * @see <a href="https://tools.ietf.org/html/rfc5849#section-3.5.1">OAuth
   * authentication</a>
   */
  OAUTH,

  /**
   * HTTP Origin-Bound Authentication (HOBA)
   *
   * @see <a href="https://tools.ietf.org/html/rfc7486">HOBA authentication</a>
   */
  HOBA,
  /**
   * Mutual Authentication Protocol for HTTP
   *
   * @see <a href="https://tools.ietf.org/html/rfc8120">Mutual
   * authentication</a>
   */
  MUTUAL,
  /**
   * @deprecated Not supported by Key Bridge
   *
   * SPNEGO-based Kerberos and NTLM HTTP Authentication in Microsoft Windows
   *
   * @see <a href="https://tools.ietf.org/html/rfc4559#section-4">Negotiate
   * authentication</a>
   */
  NEGOTIATE,
  /**
   * Salted Challenge Response Authentication Mechanism (SCRAM) SASL and GSS-API
   * Mechanisms
   *
   * @see <a href="https://tools.ietf.org/html/rfc5802">SCRAM authentication</a>
   */
  SCRAM_SHA_1,
  /**
   * SCRAM-SHA-256 and SCRAM-SHA-256-PLUS Simple Authentication and Security
   * Layer (SASL) Mechanisms
   *
   * @see <a href="https://tools.ietf.org/html/rfc7677">SCRAM 256
   * authentication</a>
   */
  SCRAM_SHA_256,
  /**
   * Voluntary Application Server Identification (VAPID) for Web Push
   *
   * @see <a href="https://tools.ietf.org/html/rfc8292">Vapid authentication</a>
   */
  VAPID;

  private static final Logger LOG = Logger.getLogger(AuthorizationType.class.getName());

  /**
   * Parse an authentication type. This converts the input string to upper case
   * and tries to match the name.
   *
   * @param type the candidate type
   * @return the corresponding enumerated type.
   */
  public static AuthorizationType parse(String type) {
    try {
      return AuthorizationType.valueOf(type.trim().toUpperCase().replaceAll("-", "_"));
    } catch (Exception e) {
      LOG.log(Level.FINE, "Unrecognized authorization type {0}", type);
      return null;
    }
  }

}
