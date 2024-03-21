package io.camunda.zeebe.spring.client.config.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.common.auth.*;
import io.camunda.common.json.JsonMapper;
import io.camunda.common.json.SdkObjectMapper;
import io.camunda.operate.CamundaOperateClient;
import io.camunda.zeebe.spring.client.configuration.CommonClientConfiguration;
import io.camunda.zeebe.spring.client.configuration.OperateClientConfiguration;
import io.camunda.zeebe.spring.client.properties.ZeebeClientConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
    properties = {
      "zeebe.client.cloud.region=syd-1",
      "zeebe.client.cloud.clusterId=cluster-id",
      "zeebe.client.cloud.clientId=client-id",
      "zeebe.client.cloud.clientSecret=client-secret",
      "camunda.operate.client.enabled=true"
    })
@ContextConfiguration(classes = OperateSaasZeebeCredentialTest.TestConfig.class)
public class OperateSaasZeebeCredentialTest {

  @ImportAutoConfiguration({CommonClientConfiguration.class, OperateClientConfiguration.class})
  @EnableConfigurationProperties(ZeebeClientConfigurationProperties.class)
  public static class TestConfig {
    @Bean
    public JsonMapper commonJsonMapper() {
      return new SdkObjectMapper();
    }
  }

  @Autowired private Authentication authentication;

  @Autowired private CamundaOperateClient operateClient;

  @Test
  public void testAuthentication() {
    assertThat(authentication).isInstanceOf(SaaSAuthentication.class);
    assertThat(operateClient).isNotNull();
  }

  @Test
  public void testCredential() {
    SaaSAuthentication saaSAuthentication = (SaaSAuthentication) authentication;
    JwtCredential jwtCredential = saaSAuthentication.getJwtConfig().getProduct(Product.OPERATE);

    assertThat(jwtCredential.getClientId()).isEqualTo("client-id");
    assertThat(jwtCredential.getClientSecret()).isEqualTo("client-secret");
  }
}