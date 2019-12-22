/*
 * Copyright 2017 Key Bridge. All rights reserved.
 * Use is subject to license terms.
 *
 * Software Code is protected by Copyrights. Author hereby reserves all rights
 * in and to Copyrights and no license is granted under Copyrights in this
 * Software License Agreement.
 *
 * Key Bridge generally licenses Copyrights for commercialization pursuant to
 * the terms of either a Standard Software Source Code License Agreement or a
 * Standard Product License Agreement. A copy of either Agreement can be
 * obtained upon request from: info@keybridgewireless.com
 */
package ch.keybridge.rs.filter.impl;

import ch.keybridge.rs.filter.CrossOrigin;
import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Cross-Origin HTTP access control (CORS). Cross-Origin Resource Sharing (CORS)
 * is a mechanism that uses additional HTTP headers to tell browsers to give a
 * web application running at one origin, access to selected resources from a
 * different origin. A web application executes a cross-origin HTTP request when
 * it requests a resource that has a different origin (domain, protocol, or
 * port) from its own.
 * <p>
 * A resource makes a cross-origin HTTP request when it requests a resource from
 * a different domain, or port than the one which the first resource itself
 * serves. For example, an HTML page served from http://domain-a.com makes an
 * &lt;img&gt; src request for http://domain-b.com/image.jpg. Many pages on the
 * web today load resources like CSS stylesheets, images and scripts from
 * separate domains.
 * <p>
 * For security reasons, browsers restrict cross-origin HTTP requests initiated
 * from within scripts. For example, XMLHttpRequest and Fetch follow the
 * same-origin policy. So, a web application using XMLHttpRequest or Fetch could
 * only make HTTP requests to its own domain. To improve web applications,
 * developers asked browser vendors to allow cross-domain requests.
 * <p>
 * The only allowed methods are: GET HEAD POST
 * <p>
 * The only headers which are allowed are: Accept Accept-Language
 * Content-Language Content-Type (but note the additional requirements below)
 * DPR Downlink Save-Data Viewport-Width Width
 * <p>
 * The only allowed values for the Content-Type header are:
 * application/x-www-form-urlencoded multipart/form-data text/plain
 *
 * @author Key Bridge LLC
 * @since v0.7.0 added 02/21/17
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS">CORS</a>
 */
@Provider
@CrossOrigin
@Priority(Priorities.HEADER_DECORATOR) // Header decorator filter/interceptor
public class CorsResponseFilter implements ContainerResponseFilter {

  /**
   * {@inheritDoc}
   * <p>
   * Add Cross-Origin Resource Sharing (CORS) headers. The Cross-Origin Resource
   * Sharing standard works by adding new HTTP headers that let servers describe
   * which origins are permitted to read that information from a web browser.
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    /**
     * Access-Control-Allow-Origin specifies either a single origin, which tells
     * browsers to allow that origin to access the resource; or else — for
     * requests without credentials — the "*" wildcard, to tell browsers to
     * allow any origin to access the resource.
     * <p>
     * The “*” means the request can come from any domain – this is the way to
     * set this header if you want to make your REST API public where everyone
     * can access it
     */
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    //  headers.add("Access-Control-Allow-Origin", "http://keybridgewireless.com"); //allows CORS requests only coming from keybridgewireless.com
//  headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    /**
     * The Access-Control-Allow-Methods header specifies the method or methods
     * allowed when accessing the resource. This is used in response to a
     * preflight request.
     * <p>
     * See: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
     */
    responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
    /**
     * The Access-Control-Allow-Headers header is used in response to a
     * preflight request to indicate which HTTP headers can be used when making
     * the actual request.
     * <p>
     * A CORS-safelisted request header is one of the following HTTP headers:
     * Accept, Accept-Language, Content-Language, Content-Type.
     * <p>
     * X-Requested-With supports AJAX requests. Most Ajax libraries (Prototype,
     * JQuery, and Dojo as of v2.1) include an X-Requested-With header that
     * indicates that the request was made by XMLHttpRequest instead of being
     * triggered by clicking a regular hyperlink or form submit button.
     */
//    responseContext.getHeaders().add("Access-Control-Allow-Headers", "Accept, Accept-Language, Content-Language, Content-Type, X-Requested-With, User-Agent, X-KeyBridge");
    responseContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, User-Agent, X-KeyBridge");
  }

}
