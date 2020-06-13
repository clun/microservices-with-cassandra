package com.datastax.sample.conf;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiDocumentationConfig {

    @Bean
    public OpenAPI openApiSpec(@Value("${springdoc.version}") String appVersion) {
        String des = "Sample REST Api leveraging on [Spring Data Cassandra](#) for repositories and "
                + " both Spring-Data-Rest and Spring MVC Rest Controllers for endpoints. Spring Data"
                + "3.0.0 introduced supports for Java Drivers 4.3+";
        return new OpenAPI()
                //.components(new Components().addSecuritySchemes("basicScheme",
                //        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .info(new Info().title("Microservices with Spring Data Cassandra")
                                .version(appVersion).description(des)
                                .termsOfService("http://swagger.io/terms/")
                                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
    
    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder().setGroup("Monitoring (Actuator)")
                .pathsToMatch("/actuator/**")
                .pathsToExclude("/actuator/health/*")
                .build();
    }
    
}
