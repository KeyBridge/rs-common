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

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * A response filter to add cache control to the header. This is a dynamically
 * assigned filter and is instantiated by the CacheControlDynamicFeature
 * (provider).
 *
 * @author Key Bridge
 * @since v0.4.0 created 12/21/19
 */
@Priority(Priorities.HEADER_DECORATOR) // Header decorator filter/interceptor
public class CacheControlFilter implements ContainerResponseFilter {

  /**
   * A CacheControl instance
   */
  private final CacheControl cacheControl;

  /**
   * Create a new CacheControlFilter with a configured cache control instance.
   *
   * @param cacheControl the CacheControl instance
   */
  public CacheControlFilter(CacheControl cacheControl) {
    this.cacheControl = cacheControl;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Apply cache control to the response header.
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    if (responseContext.getStatus() == Response.Status.OK.getStatusCode()) {
      responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
    }
  }

}
