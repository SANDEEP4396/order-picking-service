package com.egen.orderpickingservice.configuration;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfiguration {
    @Bean
    public Docket orderManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Order Picking Service API")
                .version("1.0")
                .description("API for Order Picking Service Application")
                .contact(new Contact("Sandeep Sagar Muralidhar", "https://github.com/SANDEEP4396",
                        "sandeep.sagar033@gmail.com"))
                .license("Apache License Version 2.0")
                .build();
    }
}
