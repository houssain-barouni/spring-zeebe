package io.camunda.zeebe.spring.client.properties;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.invoke.MethodHandles;

/**
 * This will be deprecated once we move to the new schema (i.e. not prefixing with camunda.*)
 */
@Deprecated
@ConfigurationProperties(prefix = "camunda.operate.client")
public class CamundaOperateClientConfigurationProperties {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Value("${zeebe.client.cloud.cluster-id:#{null}}")
  private String clusterId;

  @Value("${zeebe.client.cloud.region:bru-2}")
  private String region;

  private String clientId;
  private String clientSecret;
  private String username;
  private String password;
  private Boolean enabled = false;
  private String url;

  private String keycloakUrl;
  private String keycloakRealm = "camunda-platform";

  private String baseUrl;

  private String authUrl;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getKeycloakUrl() {
    return keycloakUrl;
  }

  public void setKeycloakUrl(String keycloakUrl) {
    this.keycloakUrl = keycloakUrl;
  }

  public String getKeycloakRealm() {
    return keycloakRealm;
  }

  public void setKeycloakRealm(String keycloakRealm) {
    this.keycloakRealm = keycloakRealm;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public void setAuthUrl(String authUrl) {
    this.authUrl = authUrl;
  }

  public String getOperateUrl() {
    if (url != null) {
      LOG.debug("Connecting to Camunda Operate on URL: " +url);
      return url;
    } else if (clusterId != null) {
      String url = "https://" + region + "." + getFinalBaseUrl() + "/" + clusterId + "/";
      LOG.debug("Connecting to Camunda Operate SaaS via URL: " + url);
      return url;
    }
    throw new IllegalArgumentException(
      "In order to connect to Camunda Operate you need to specify either a SaaS clusterId or an Operate URL.");
  }

  private String getFinalBaseUrl() {
    if (getBaseUrl() != null) {
      return getBaseUrl();
    } else {
      return "operate.camunda.io";
    }  }

  @PostConstruct
  private void applyFinalValues() {
    if (this.getClientId() != null && this.getClientSecret() != null) {
      this.setEnabled(true);
    } else if (this.getUsername() != null && this.getPassword() != null) {
      this.setEnabled(true);
    } else if (this.getAuthUrl() != null || this.getBaseUrl() != null || this.getUrl() != null) {
      this.setEnabled(true);
    }
  }
}
