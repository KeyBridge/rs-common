/*
 * Copyright 2019 Key Bridge. All rights reserved. Use is subject to license
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
package ch.keybridge.rs.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Set the ETag response header. The ETag HTTP response header is an identifier
 * for a specific version of a resource. It lets caches be more efficient and
 * save bandwidth, as a web server does not need to resend a full response if
 * the content has not changed. Additionally, etags help prevent simultaneous
 * updates of a resource from overwriting each other ("mid-air collisions").
 * <p>
 * If the resource at a given URL changes, a new Etag value must be generated. A
 * comparison of them can determine whether two representations of a resource
 * are the same. Etags are therefore similar to fingerprints, and might also be
 * used for tracking purposes by some servers. They might also be set to persist
 * indefinitely by a tracking server.
 * <p>
 * The "ETag" header field in a response provides the current entity-tag for the
 * selected representation, as determined at the conclusion of handling the
 * request. An entity-tag is an opaque validator for differentiating between
 * multiple representations of the same resource, regardless of whether those
 * multiple representations are due to resource state changes over time, content
 * negotiation resulting in multiple representations being valid at the same
 * time, or both. An entity-tag consists of an opaque quoted string, possibly
 * prefixed by a weakness indicator.
 * <p>
 * Instructions:
 * <p>
 * You can manually set the ETag by setting a response header "ETAG", which will
 * be picked up by the filter and set. A manual ETag should ALWAYS be used for
 * large message body content. Otherwise, an ETag is calculated as the MD5 hash
 * of the message body content.
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/ETag">ETag</a>
 * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">RFC 7232,
 * section 2.3: ETag Hypertext Transfer Protocol (HTTP/1.1): Conditional
 * Requests</a>
 * @author Key Bridge
 * @since v0.4.0 created 12/21/19
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Etag {

  /**
   * W/ Optional 'W/' (case-sensitive) indicates that a weak validator is used.
   * Weak etags are easy to generate, but are far less useful for comparisons.
   * Strong validators are ideal for comparisons but can be very difficult to
   * generate efficiently. Weak ETag values of two representations of the same
   * resources might be semantically equivalent, but not byte-for-byte
   * identical. This means weak etags prevent caching when byte range requests
   * are used, but strong etags mean range requests can still be cached. For
   * example:
   * <p>
   * ETag: W/"0815" versus <br>
   * ETag: "33a64df551425fcc55e4d42a148795d9f25f89d4"
   *
   * @return TRUE indicates that a weak validator is used
   */
  boolean weak() default false;

}
