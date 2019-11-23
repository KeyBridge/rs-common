/*
 * Copyright 2015 Caulfield IP Holdings (Caulfield) and affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * Software Code is protected by copyright. Caulfield hereby
 * reserves all rights and copyrights and no license is
 * granted under said copyrights in this Software License Agreement.
 * Caulfield generally licenses software for commercialization
 * pursuant to the terms of either a Standard Software Source Code
 * License Agreement or a Standard Product License Agreement.
 * A copy of these agreements may be obtained by sending a request
 * via email to info@caufield.org.
 */
package ch.keybridge.rs.filter.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * Server logging filter based on JavaEE 7 example.
 * <p>
 * By default, a filter is globally bound. That means it is applied to all
 * resources and all methods within that resource.
 * <p>
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * @author Arun Gupta
 * @author Key Bridge LLC
 */
//@Provider
//@Priority(Priorities.USER)
public class ServerLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  /**
   * Find or create a logger for a named subsystem. If a logger has already been
   * created with the given name it is returned. Otherwise a new logger is
   * created.
   */
  private static final Logger logger = Logger.getLogger(ServerLoggingFilter.class.getName());

  @Override
  public void filter(ContainerRequestContext crc) throws IOException {
    logger.log(Level.INFO, "Server HTTP QUERY    {0}  {1}  {2}", new Object[]{crc.getMethod(), crc.getUriInfo().getAbsolutePath(), crc.getHeaders()});
//    System.out.println("<start>ContainerRequestFilter");
//    System.out.println(crc.getMethod() + " " + crc.getUriInfo().getAbsolutePath());
//    for (String key : crc.getHeaders().keySet()) {      System.out.println(key + ": " + crc.getHeaders().get(key));    }
//    crc.getHeaders().add("serverHeader", "serverHeaderValue");
//    System.out.println("<end>ContainerRequestFilter");
  }

  @Override
  public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {
    logger.log(Level.INFO, "Server HTTP RESPONSE  {0}: {1}  {2}", new Object[]{crc.getMethod(), crc.getUriInfo().getAbsolutePath(), crc1.getHeaders()});
//    logger.log(Level.INFO, "Server HTTP RESPONSE {0}", new Object[]{crc1.getHeaders().entrySet()});
//    System.out.println("<start>ContainerResponseFilter");
//    for (String key : crc1.getHeaders().keySet()) {      System.out.println(key + ": " + crc1.getHeaders().get(key));    }
//    System.out.println("<end>ContainerResponseFilter");
  }
}
