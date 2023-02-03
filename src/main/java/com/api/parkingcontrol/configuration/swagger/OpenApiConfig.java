package com.api.parkingcontrol.configuration.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Controle de Vagas de Estacionamento")
                .description(
                    "API para Controle de vagas de estacionamento, usando Security, JPA e Swagger")
                .version("V0.0.1")
                .contact(new Contact()
                    .name("André S Nascimento")
                    .url("https://github.com/andre-s-nascimento")
                    .email("andresoaresnascimento@gmail.com")
                )
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
        .externalDocs(
            new ExternalDocumentation()
                .description("Documentação do Controle de Vagas de Estacionamento")
                .url("#"));
  }
}
