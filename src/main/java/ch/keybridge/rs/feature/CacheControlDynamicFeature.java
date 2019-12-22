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
package ch.keybridge.rs.feature;

import ch.keybridge.rs.filter.CacheControlling;
import ch.keybridge.rs.filter.impl.CacheControlFilter;
import java.lang.reflect.Method;
import javax.ws.rs.GET;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * DynamicFeature implementation to apply the CacheControlFilter to REST classes
 * or methods annotated with the @CacheControl interface.
 *
 * @author Key Bridge
 * @since v0.4.0 created 12/21/19
 */
@Provider
public class CacheControlDynamicFeature implements DynamicFeature {

  /**
   * {@inheritDoc}
   * <p>
   * Bind filters to resource methods programmatically. With dynamic binding we
   * can implement code which defines the bindings during the application
   * initialization time. To achieve dynamic binding, we need to define a
   * feature (extending a class from DynamicFeature and annotate it with
   *
   * @Provider) and programmatically register our filter (the Filter should not
   * use @Provider).
   */
  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    /**
     * Get the declaring class or method. Abort if either is null.
     */
    final Class<?> declaring = resourceInfo.getResourceClass();
    final Method method = resourceInfo.getResourceMethod();
    if (declaring == null || method == null) {
      return;
    }
    /**
     * Only apply cache control to GET methods.
     */
    if (!method.isAnnotationPresent(GET.class)) {
      return;
    }
    /**
     * Get the declaring anotation instances.
     */
    CacheControlling classCache = declaring.getAnnotation(CacheControlling.class);
    CacheControlling methodCache = method.getAnnotation(CacheControlling.class);
    /**
     * Build a CacheControl instance based on the method or class annotation,
     * prioritizing the method annotation if both are present.
     */
    CacheControl cacheControl = null;
    if (methodCache != null) {
      cacheControl = buildCacheControl(methodCache);
    } else if (classCache != null) {
      cacheControl = buildCacheControl(classCache);
    }
    /**
     * Fail gracefully.
     */
    if (cacheControl != null) {
      context.register(new CacheControlFilter(cacheControl));
    }

  }

  /**
   * Create and configure a CacheControl instance, reading the configuration
   * from the @Cache annotation.
   *
   * @param cache The cache annotation instance
   * @return A CacheControl configuration
   */
  private CacheControl buildCacheControl(CacheControlling cache) {
    CacheControl cacheControl = new CacheControl();
    /**
     * If no-cache is set then simply declare no-cache and return.
     */
    if (cache.noCache()) {
      cacheControl.setNoCache(true);
      return cacheControl;
    }
    /**
     * Otherwise configure the cache-control.
     */
    if (cache.isPrivate()) {
      cacheControl.setPrivate(true);
    }
    /**
     * Only set the max-age and s-max-age if the numbers are zero or positive.
     */
    if (cache.maxAge() > -1) {
      cacheControl.setMaxAge(cache.maxAge());
    }
    if (cache.sMaxAge() > -1) {
      cacheControl.setSMaxAge(cache.sMaxAge());
    }
    /**
     * Set the rest.
     */
    cacheControl.setMustRevalidate((cache.mustRevalidate()));
    cacheControl.setNoStore((cache.noStore()));
    cacheControl.setNoTransform((cache.noTransform()));
    cacheControl.setProxyRevalidate(cache.proxyRevalidate());
    return cacheControl;
  }

}
