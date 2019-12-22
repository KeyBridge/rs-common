/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.keybridge.rs.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Set the Cache-Control response header. The directives are case-insensitive
 * and have an optional argument, that can use both token and quoted-string
 * syntax. Multiple directives are comma-separated.
 * <p>
 * Cache response directives: Standard Cache-Control directives that can be used
 * by the server in an HTTP response are:
 * <p>
 * Cache-Control: must-revalidate <br>
 * Cache-Control: no-cache <br>
 * Cache-Control: no-store <br>
 * Cache-Control: no-transform <br>
 * Cache-Control: public <br>
 * Cache-Control: private <br>
 * Cache-Control: proxy-revalidate <br>
 * Cache-Control: max-age=seconds <br>
 * Cache-Control: s-maxage=seconds <br>
 * <p>
 * The Cache-Control general-header field is used to specify directives for
 * caching mechanisms in both requests and responses. Caching directives are
 * unidirectional, meaning that a given directive in a request is not implying
 * that the same directive is to be given in the response.
 *
 * @see
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control">Cache-Control</a>
 * @author Key Bridge
 * @since v0.4.0 created 12/21/19
 */
@NameBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControlling {

  /**
   * no-cache Forces caches to submit the request to the origin server for
   * validation before releasing a cached copy.
   * <p>
   * Note: If true then all other cache configurations are ignored.
   *
   * @return the no-cache directive
   */
  boolean noCache() default false;

  /**
   * max-age=seconds Specifies the maximum amount of time a resource will be
   * considered fresh. Contrary to Expires, this directive is relative to the
   * time of the request.
   * <p>
   * Default is 1 day = 86400 seconds
   *
   * @return the max-age directive
   */
  int maxAge() default 86400;

  /**
   * s-maxage=seconds Takes precedence over max-age or the Expires header, but
   * it only applies to shared caches (e.g., proxies) and is ignored by a
   * private cache.
   *
   * @return the s-maxage directive
   */
  int sMaxAge() default -1;

  /**
   * no-store The cache should not store anything about the client request or
   * server response.
   *
   * @return the no-store directive
   */
  boolean noStore() default false;

  /**
   * no-transform No transformations or conversions should be made to the
   * resource. The Content-Encoding, Content-Range, Content-Type headers must
   * not be modified by a proxy. A non-transparent proxy or browser feature such
   * as Google's Light Mode might, for example, convert between image formats in
   * order to save cache space or to reduce the amount of traffic on a slow
   * link. The no-transform directive disallows this.
   *
   * @return the no-transform directive
   */
  boolean noTransform() default false;

  /**
   * must-revalidate Indicates that once a resource has become stale (e.g.
   * max-age has expired), a cache must not use the response to satisfy
   * subsequent requests for this resource without successful validation on the
   * origin server.
   *
   * @return the must-revalidate directive
   */
  boolean mustRevalidate() default false;

  /**
   * proxy-revalidate Same as must-revalidate, but it only applies to shared
   * caches (e.g., proxies) and is ignored by a private cache.
   *
   * @return the proxy-revalidate directive
   */
  boolean proxyRevalidate() default false;

  /**
   * private Indicates that the response is intended for a single user and must
   * not be stored by a shared cache. A private cache may store the response.
   *
   * @return the private directive
   */
  boolean isPrivate() default false;

}
