package ch.keybridge.rs.filter.impl;

import ch.keybridge.rs.filter.HttpAuthorization;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * HTTP role based authorization (AuthZ) filter. Reads and implements the
 * JSR-250 annotations 'RolesAllowed', 'PermitAll' and 'DenyAll'. Implements
 * logic from the Jersey RolesAllowedDynamicFeature.
 * <p>
 * To us: Annotate your REST class with 'HttpAuthorization'.
 *
 * @see
 * <a href="https://github.com/jersey/jersey/blob/master/core-server/src/main/java/org/glassfish/jersey/server/filter/RolesAllowedDynamicFeature.java">RolesAllowedDynamicFeature</a>
 * @see
 * <a href="https://docs.oracle.com/javaee/6/api/javax/annotation/security/package-summary.html">Security</a>
 * @see <a href="https://jcp.org/en/jsr/detail?id=250">JSR 250: Common
 * Annotations for the JavaTM Platform</a>
 * @author Key Bridge
 * @since v0.8.0 created 2020-08-24
 */
@Provider
@Dependent
@HttpAuthorization
@Priority(Priorities.AUTHORIZATION)
public class HttpAuthorizationFilter implements ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(HttpAuthorizationFilter.class.getName());

  /**
   * Provides access the resource class and resource method matched by the
   * current request.
   */
  @Context
  private ResourceInfo resourceInfo;

  /**
   * {@inheritDoc}
   * <p>
   * DenyAll takes precedence over RolesAllowed and PermitAll on amethod <br>
   * RolesAllowed takes precedence over PermitAll on a method  <br>
   * PermitAll takes precedence over RolesAllowed on a method <br>
   * DenyAll can't be attached to a class.  <br>
   * RolesAllowed takes precedence over PermitAll on a class
   */
  @Override
  public void filter(final ContainerRequestContext requestContext) throws IOException {
    /**
     * Get the resource method that is the target of a request.
     */
    Method resourceMethod = resourceInfo.getResourceMethod();
    /**
     * 'DenyAll' specifies that no security roles are allowed to invoke the
     * specified method(s) - i.e that the methods are to be excluded from
     * execution in the J2EE container.
     */
    if (resourceMethod.isAnnotationPresent(DenyAll.class)) {
      LOG.log(Level.FINE, "DenyAll request '{'method={0}'}'", resourceMethod.getName());
      throw new WebApplicationException(Response.Status.FORBIDDEN);
    }
    /**
     * `RolesAllowed` specifies the list of roles permitted to access method(s)
     * in an application. The value of the RolesAllowed annotation is a list of
     * security role names.
     */
    RolesAllowed rolesAllowed = resourceMethod.getAnnotation(RolesAllowed.class);
    if (rolesAllowed != null) {
      performAuthorization(rolesAllowed.value(), requestContext);
      return;
    }
    /**
     * 'PermitAll' specifies that all security roles are allowed to invoke the
     * specified method(s) i.e that the specified method(s) are "unchecked". It
     */
    if (resourceMethod.isAnnotationPresent(PermitAll.class)) {
      return; // Do nothing
    }
    /**
     * DenyAll can't be attached to classes. RolesAllowed on the class takes
     * precedence over PermitAll on the class
     */
    rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
    if (rolesAllowed != null) {
      performAuthorization(rolesAllowed.value(), requestContext);
    }
    /**
     * @PermitAll on the class
     */
    if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
      return;  // Do nothing
    }
    /**
     * Authentication is required for non-annotated methods
     */
    if (!isAuthenticated(requestContext)) {
      LOG.log(Level.FINE, "Unuthenticated request '{'method={0}'}'", resourceMethod.getName());
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
  }

  /**
   * Perform authorization based on roles.
   *
   * @param rolesAllowed   the list of roles permitted to access method(s) in an
   *                       application
   * @param requestContext the Container request filter context.
   */
  private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) {
    /**
     * Basic sanity check before processing.
     */
    if (rolesAllowed.length > 0 && !isAuthenticated(requestContext)) {
      LOG.log(Level.FINE, "Unuthenticated user request '{'method={0}, rolesAllowed={1}'}'", new Object[]{resourceInfo.getResourceMethod().getName(), Arrays.toString(rolesAllowed)});
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    /**
     * Return if the user is in the role.
     */
    for (final String role : rolesAllowed) {
      if (requestContext.getSecurityContext().isUserInRole(role)) {
        System.out.println("debug MATCH isUserInRole " + role);
        return;
      }
    }
    /**
     * The user is authenticated but does not have the required role.
     */
    LOG.log(Level.INFO, "Authenticated user not in rolesAllowed {0}", Arrays.toString(rolesAllowed));
    throw new WebApplicationException(Response.Status.FORBIDDEN);
  }

  /**
   * Check if the user is authenticated.
   *
   * @param requestContext the Container request filter context.
   * @return TRUE if the user security principal is configured, false if null
   */
  private boolean isAuthenticated(final ContainerRequestContext requestContext) {
    return requestContext.getSecurityContext().getUserPrincipal() != null;
  }
}
