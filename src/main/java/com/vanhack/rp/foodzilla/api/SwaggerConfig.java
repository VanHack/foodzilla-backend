package com.vanhack.rp.foodzilla.api;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vanhack.rp.foodzilla.api"))
                .paths(regex("/api.*"))
                .build()
                .apiInfo(metaData());
    }
    
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Foodzilla API",
                "Foodzilla API delivering services for the Foodzilla applications.",
                "1.0",
                "",
//                new Contact("Byond", "https://getbyond.com", "contact@getbyond.com"),
                "",
                "",
                "");
        return apiInfo;
    }
}