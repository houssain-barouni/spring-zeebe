package io.camunda.common.auth;

import io.camunda.common.auth.identity.IdentityConfig;

public class SelfManagedAuthenticationBuilder {

  SelfManagedAuthentication selfManagedAuthentication;

  SelfManagedAuthenticationBuilder() {
    selfManagedAuthentication = new SelfManagedAuthentication();
  }

  public SelfManagedAuthenticationBuilder jwtConfig(JwtConfig jwtConfig) {
    selfManagedAuthentication.setJwtConfig(jwtConfig);
    return this;
  }

//  public SelfManagedAuthenticationBuilder keycloakUrl(String keycloakUrl) {
//    selfManagedAuthentication.setKeycloakUrl(keycloakUrl);
//    return this;
//  }

//  public SelfManagedAuthenticationBuilder keycloakRealm(String keycloakRealm) {
//    if (keycloakRealm != null) {
//      selfManagedAuthentication.setKeycloakRealm(keycloakRealm);
//    }
//    return this;
//  }

//  public SelfManagedAuthenticationBuilder keycloakTokenUrl(String keycloakTokenUrl) {
//    if (keycloakTokenUrl != null) {
//      selfManagedAuthentication.setKeycloakTokenUrl(keycloakTokenUrl);
//    }
//    return this;
//  }

  public SelfManagedAuthenticationBuilder identityConfig(IdentityConfig identityConfig) {
    selfManagedAuthentication.setIdentityConfig(identityConfig);
    return this;
  }

  public Authentication build() {
    return selfManagedAuthentication.build();
  }
}
