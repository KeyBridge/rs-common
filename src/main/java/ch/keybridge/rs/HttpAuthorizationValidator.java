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
package ch.keybridge.rs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

/**
 * HTTP authorization validator.
 * <p>
 * The HTTP Authorization request header contains the credentials to
 * authenticate a user agent with a server, usually, but not necessarily, after
 * the server has responded with a 401 Unauthorized status and the
 * WWW-Authenticate header.
 * <p>
 * Syntax is {@code Authorization: <type> <credentials>}
 *
 * @author Key Bridge
 * @since v0.7.0 created 2020-08-12
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization">HTTP
 * Authorization</a>
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
public interface HttpAuthorizationValidator {

  /**
   * Validate the authorization. Produces a SecurityContext implementation that
   * provides access to security related information for the indicated
   * authorization detail.
   * <p>
   * Note that the security context 'secure' value is set to false.
   *
   * @param type        the authentication type. A common type is "Basic". Other
   *                    types include [Basic, Bearer, Digest, HOBA, Mutual,
   *                    Negotiate, OAuth, SCRAM-SHA-1, SCRAM-SHA-256, vapid]
   * @param credentials the authentication credential. This typically encodes an
   *                    access key ID and scope information.
   * @return TRUE if the credentials are valid, otherwise false
   * @see
   * <a href="http://www.iana.org/assignments/http-authschemes/http-authschemes.xhtml">HTTP
   * Authentication Scheme Registry</a>
   * @throws WebApplicationException if validation fails or the user is not
   *                                 authorized
   */
//  SecurityContext validate(AuthorizationType type, String credentials) throws WebApplicationException;
  /**
   * Validate the authorization. Produces a SecurityContext implementation that
   * provides access to security related information for the indicated
   * authorization detail.
   *
   * @param type        the authentication type. A common type is "Basic". Other
   *                    types include [Basic, Bearer, Digest, HOBA, Mutual,
   *                    Negotiate, OAuth, SCRAM-SHA-1, SCRAM-SHA-256, vapid]
   * @param credentials the authentication credential. This typically encodes an
   *                    access key ID and scope information.
   * @param secure      boolean indicating whether this request was made using a
   *                    secure channel, such as HTTPS.
   * @return the SecurityContext implementation for the user corresponding to
   *         the provided credentials
   */
  SecurityContext validate(AuthorizationType type, String credentials, boolean secure) throws WebApplicationException;
}
