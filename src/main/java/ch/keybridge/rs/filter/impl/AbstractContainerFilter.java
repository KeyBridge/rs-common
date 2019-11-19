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

import java.io.*;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * An abstract container filter based on JavaEE 7 example. Provides messages to
 * read data from request and response context.
 * <p>
 * By default, a filter is globally bound. That means it is applied to all
 * resources and all methods within that resource.
 * <p>
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * @author Arun Gupta
 * @author Key Bridge LLC
 */
public abstract class AbstractContainerFilter {

  @SuppressWarnings("NonConstantLogger")
  protected final Logger LOG;

  public AbstractContainerFilter() {
    LOG = Logger.getLogger(this.getClass().getName());
  }

  /**
   * Get the HTTP request entity body.
   *
   * @param requestContext the request
   * @return the data
   */
  protected String readRawData(ContainerRequestContext requestContext) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in = requestContext.getEntityStream();
    final StringBuilder b = new StringBuilder();
    try {
      writeTo(in, out);
      byte[] requestEntity = out.toByteArray();
      if (requestEntity.length == 0) {
        b.append("").append("\n");
      } else {
        b.append(new String(requestEntity)).append("\n");
      }
      /**
       * Reset the byte stream.
       */
      requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
    } catch (IOException ex) {
      //Handle logging error
    }
    return b.toString();
  }

  /**
   * Get the HTTP response entity body.
   *
   * @param responseContext the response
   * @return the data
   */
//  protected String readJsonData(ContainerResponseContext responseContext) {
//    try {
//      /**
//       * If a string, return the content.
//       */
//      if (String.class.equals(responseContext.getEntityClass())) {
//        return (String) responseContext.getEntity();
//      }
//      /**
//       * Else try to marshal to JSON.
//       */
//      return JsonUtility.marshal(responseContext.getEntity());
//    } catch (IOException e) {
////      LOG.log(Level.SEVERE, null, e);
//      LOG.severe("Error marshaling response content to JSON.");
//      return responseContext.getEntity().toString();
//    }
//  }
  /**
   * Read bytes from an input stream and write them to an output stream.
   * <p>
   * From org.glassfish.jersey.message.internal.ReaderWriter;
   *
   * @param in  the input stream to read from.
   * @param out the output stream to write to.
   * @throws IOException if there is an error reading or writing bytes.
   */
  protected void writeTo(InputStream in, OutputStream out) throws IOException {
    int read;
    final byte[] data = new byte[1024];
    while ((read = in.read(data)) != -1) {
      out.write(data, 0, read);
    }
  }

}
