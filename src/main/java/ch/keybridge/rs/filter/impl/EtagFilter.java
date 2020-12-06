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
package ch.keybridge.rs.filter.impl;

import ch.keybridge.rs.filter.Etag;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

/**
 * A response filter to calculate and add an ETag to the header.
 * <p>
 * An entity-tag can be either a weak or strong validator, with strong being the
 * default. If an origin server provides an entity-tag for a representation and
 * the generation of that entity-tag does not satisfy all of the characteristics
 * of a strong validator (Section 2.1), then the origin server MUST mark the
 * entity-tag as weak by prefixing its opaque value with "W/" (case-sensitive).
 *
 * @author Key Bridge
 * @since v0.4.0 created 12/21/19
 */
@Provider
@Etag
@Priority(Priorities.HEADER_DECORATOR) // Header decorator filter/interceptor
public class EtagFilter extends AbstractContainerFilter implements ContainerResponseFilter {

  /**
   * {@inheritDoc}
   * <p>
   * Adds an ETag to the response header. If `weak` is selected then will only
   * read and mark the first 1000 bytes of the response stream.
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    /**
     * Short circuit if the ETag is already set.
     */
    if (responseContext.getEntityTag() != null) {
      return;
    }
    /**
     * The entity tag, which will be populated one way or another.
     */
    EntityTag etag;
    /**
     * If the response placed an ETAG header then use it.
     */
    if (responseContext.getHeaders().containsKey(HttpHeaders.ETAG)) {
      etag = new EntityTag(responseContext.getHeaderString(HttpHeaders.ETAG));
      responseContext.getHeaders().remove(HttpHeaders.ETAG);
    } else if (responseContext.getEntity() != null) {
      /**
       * Calculate the e-tag from the message request URI plus today's date. Try
       * to set a strong tag as an MD5 hash of the response body.
       */
      URI requestUri = requestContext.getUriInfo().getRequestUri();
      /**
       * Try to build a strong EntityTag, but set a weak one if the hashing
       * function fails.
       */
      try {
        etag = new EntityTag(md5Hash(requestUri, LocalDate.now()));
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        LOG.log(Level.WARNING, "MD5 hash error:  {0}", noSuchAlgorithmException.getMessage());
        etag = new EntityTag(String.valueOf(Objects.hash(requestUri, LocalDate.now())), true);
      }
    } else {
      /**
       * The response content is null. Do not set an entity tag.
       */
      etag = null;
    }
    /**
     * Conditionally set the EntityTag.
     */
    if (etag != null) {
      responseContext.getHeaders().putSingle(HttpHeaders.ETAG, etag);
    }
  }

  /**
   * Evaluate an MD5 hash.
   *
   * @param data the text data
   * @return an md5 hash of the text
   * @throws NoSuchAlgorithmException if the MD5 hash is not supported by the
   *                                  operating system
   */
  private String md5Hash(Object... data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    for (Object object : data) {
      md.update(String.valueOf(object).getBytes());
    }
    byte[] digest = md.digest();
    return DatatypeConverter.printHexBinary(digest).toUpperCase();
  }

  /**
   * Safe method to evaluate an MD5 hash. If MD5 is not supported then returns
   * the simple Java Objects hash.
   *
   * @param data the text data
   * @return an md5 hash of the text
   */
//  private String md5(String data) {
  private String md5(Object... data) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      for (Object object : data) {
        md.update(String.valueOf(object).getBytes());
      }
      byte[] digest = md.digest();
      return DatatypeConverter.printHexBinary(digest).toUpperCase();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      LOG.log(Level.WARNING, "MD5 hash error:  {0}", noSuchAlgorithmException.getMessage());
      return String.valueOf(Objects.hash(data));

    }
  }

}
