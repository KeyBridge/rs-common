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
package ch.keybridge.rs;

import ch.keybridge.rs.filter.impl.ClientLoggingFilter;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * An abstract REST client, implementing all the basics to build a useful REST
 * client instance.
 *
 * @author Key Bridge
 * @since v0.3.0 copied from lib-rest-client
 */
public abstract class AbstractRestClient {

  /**
   * Connect timeout interval, in milliseconds. The value MUST be an instance
   * convertible to Integer. A value of zero (0) is equivalent to an interval of
   * infinity. The default value is infinity (0).
   * <p>
   * From org.glassfish.jersey.client.ClientProperties
   * <p>
   * CONNECT_TIMEOUT = "jersey.config.client.connectTimeout";
   */
  public static final String READ_TIMEOUT = "jersey.config.client.readTimeout";
  /**
   * Read timeout interval, in milliseconds. The value MUST be an instance
   * convertible to Integer. A value of zero (0) is equivalent to an interval of
   * infinity. The default value is infinity (0).
   * <p>
   * From org.glassfish.jersey.client.ClientProperties
   * <p>
   * READ_TIMEOUT = "jersey.config.client.readTimeout";
   */
  public static final String CONNECT_TIMEOUT = "jersey.config.client.connectTimeout";
  /**
   * 1,000 milliseconds = 1 seconds.
   * <p>
   * Connect timeout interval, in milliseconds. The value MUST be an instance
   * convertible to Integer. A value of zero (0) is equivalent to an interval of
   * infinity. The default value is infinity (0).
   */
  private static final int TIMEOUT_CONNECT = 1000;
  /**
   * 5,000 milliseconds = 5 seconds.
   * <p>
   * Read timeout interval, in milliseconds. The value MUST be an instance
   * convertible to Integer. A value of zero (0) is equivalent to an interval of
   * infinity. The default value is infinity (0).
   */
  private static final int TIMEOUT_READ = 5000;

  /**
   * The Connect timeout interval, in milliseconds. Default is 1,000
   * milliseconds = 1 seconds.
   */
  private int timoutConnect;
  /**
   * The Read timeout interval, in milliseconds. Default is 5,000 milliseconds =
   * 5 seconds.
   */
  private int timoutRead;

  /**
   * Enable or disable client logging. Default is enabled.
   */
  private boolean clientLogging = true;

  /**
   * Default no-arg constructor. Sets the connect timeout to 1 second and read
   * timeout to 5 seconds.
   */
  public AbstractRestClient() {
    this.timoutConnect = TIMEOUT_CONNECT;
    this.timoutRead = TIMEOUT_READ;
  }

  /**
   * New constructor.
   *
   * @param timoutConnect Connect timeout interval, in milliseconds.
   * @param timoutRead    Read timeout interval, in milliseconds.
   */
  public AbstractRestClient(int timoutConnect, int timoutRead) {
    this.timoutConnect = timoutConnect;
    this.timoutRead = timoutRead;
  }

  /**
   * Enable or disable client logging. Default is enabled.
   *
   * @param clientLogging TRUE to enable, false to disable.
   */
  public void setClientLogging(boolean clientLogging) {
    this.clientLogging = clientLogging;
  }

  /**
   * Set the Connect timeout interval, in milliseconds. Default is 1,000
   * milliseconds = 1 seconds.
   *
   * @param timoutConnect The Connect timeout interval
   */
  public void setTimoutConnect(int timoutConnect) {
    this.timoutConnect = timoutConnect;
  }

  /**
   * Set the Read timeout interval, in milliseconds. Default is 5,000
   * milliseconds = 5 seconds.
   *
   * @param timoutRead The Read timeout interval
   */
  public void setTimoutRead(int timoutRead) {
    this.timoutRead = timoutRead;
  }

  /**
   * Internal method to generate a standard Jersey HTTP client with a defined
   * socket read timeout.
   * <p>
   * Unless otherwise specified in the constructor, the default TCP connect
   * timeout is 1 second (down from the default of 60) and the read timeout is 5
   * seconds (also down from 60).
   *
   * @return a Jersey HTTP client
   */
  protected Client buildClient() {
    /**
     * Internal method to build a simple web-target attached to the
     * WEBSERVICE_BASE. The connect and read timeout are set.
     *
     * @return a WebTarget instance ready to use.
     */
    Client client = ClientBuilder.newClient();
    /**
     * If transaction logging is enabled the register the client logging filter.
     */
    if (clientLogging) {
      client.register(ClientLoggingFilter.class, ClientResponseFilter.class);
    }
    /**
     * Connect timeout interval, in milliseconds. The value MUST be an instance
     * convertible to Integer. A value of zero (0) is equivalent to an interval
     * of infinity. The default value is infinity (0).
     */
    client.property(CONNECT_TIMEOUT, timoutConnect);
    /**
     * Read timeout interval, in milliseconds. The value MUST be an instance
     * convertible to Integer. A value of zero (0) is equivalent to an interval
     * of infinity. The default value is infinity (0).
     */
    client.property(READ_TIMEOUT, timoutRead);
    return client;
  }

  /**
   * Create a Client with a relaxed X509 certificate and hostname verification
   * while using the SSL over the HTTP protocol.
   * <p>
   * Unless otherwise specified in the constructor, the default TCP connect
   * timeout is 1 second (down from the default of 60) and the read timeout is 5
   * seconds (also down from 60).
   *
   * @return a Jersey HTTP client
   * @throws Exception if TLSv1 is not supported
   */
  protected Client buildTrustingClient() throws Exception {
    /**
     * Set the default X509 Trust Manager to an instance of a fake class that
     * trust all certificates, even the self-signed ones.
     */
    SSLContext sc = SSLContext.getInstance("TLSv1.2"); //Java 8   // NoSuchAlgorithmException
    System.setProperty("https.protocols", "TLSv1.2");  //Java 8
    TrustManager[] trustAllCerts = {new InsecureTrustManager()};
    sc.init(null, trustAllCerts, new java.security.SecureRandom()); // KeyManagementException
    /**
     * A fake hostname verifier, trusting any host name. Always return true,
     * indicating that the host name is an acceptable match with the server's
     * authentication scheme.
     */
    HostnameVerifier allHostsValid = (String string, SSLSession ssls) -> true;
    Client client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
    client.property(CONNECT_TIMEOUT, timoutConnect); // should immediately connect
    client.property(READ_TIMEOUT, timoutRead); // wait for processing
    /**
     * If transaction logging is enabled the register the client logging filter.
     */
    if (clientLogging) {
      client.register(ClientLoggingFilter.class, ClientResponseFilter.class);
    }
    return client;
  }

  /**
   * Manage which X509 certificates may be used to authenticate the remote side
   * of a secure socket. This class allow any X509 certificates to be used to
   * authenticate the remote side of a secure socket, including self-signed
   * certificates.
   */
  private class InsecureTrustManager implements X509TrustManager {

    /**
     * {@inheritDoc}
     * <p>
     * Always trust for client SSL chain peer certificate chain with any
     * authType authentication types.
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
      // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     * <p>
     * Always trust for server SSL chain peer certificate chain with any
     * authType exchange algorithm types.
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
      // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns an empty array of certificate authority certificates which are
     * trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[]{};
    }
  }

}
