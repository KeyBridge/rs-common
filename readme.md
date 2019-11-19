# lib-rs-filter

**RESTful web service filter implementations.**

This library provides filter interfaces that may be applied to any JAX-RS (Jersey)
REST service. Filters are applied by annotation.

Includes reference implementations of the following RESTful protocols and strategies:

* MessageAddressing - REST implementation of SOAP Web Services Addressing 1.0 (WS-Addressing) protocol


## Binding JAX-RS Providers to Resource Methods

When you register providers (such as filters and interceptors) in your application they’re associated and applied to each resource method by default (bound globally). For most cases this approach is sufficient but sometimes, especially when you want to use your provider only with a few methods, JAX-RS 2.0 supports Name and Dynamic Binding.

### Name Binding

Name Binding is an annotation-driven (static) provider binding based on the JAX-RS meta-annotation `@NameBinding`. Custom binding annotations annotated with _@NameBinding_ can be later used to decorate resource classes or resource methods as well as JAX-RS providers to define associations between them.
For example, a logging filter to record incoming and outgoing communication (headers and/or entity) on our server.

```java
@Provider
@Logged
public class LoggingFilter implements ContainerRequestFilter,
                                      ContainerResponseFilter { ... }
```

Decorating this class with _@Logged_ (name binding) annotation we transformed a globally-bound provider to a name-bound one. To bind this provider to a resource method we need to decorate the method with _@Logged_ as well:

```java
@Path("helloworld")
public class HelloWorldResource {

    @GET
    @Produces("text/plain")
    @Logged
    public String getHello() {
        return "Hello World!";
    }
}
```

Finally to create a name binding annotation from @Logged just put @NameBinding on top of it:

```java
@NameBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Logged { }
```

## References

* [Web Services Addressing](https://www.w3.org/TR/ws-addr-core)
* [Binding JAX-RS Providers](https://blog.dejavu.sk/binding-jax-rs-providers-to-resource-methods/)
* [Jersey JAX-RS (JSR-370)](https://eclipse-ee4j.github.io/jersey/)
  *  [Name binding](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/filters-and-interceptors.html#d0e9675)
  *  [Dynamic binding](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/filters-and-interceptors.html#d0e9748)

