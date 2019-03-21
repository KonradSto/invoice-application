package pl.coderstrust.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.coderstrust.controller"))
            .paths(PathSelectors.ant("/invoices/**"))
            .build()
            .consumes(Collections.singleton("application/json"))
            .produces(Collections.singleton("application/json"))
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            "Invoices REST API",
            "Some custom description of API.",
            "API TOS",
            "Terms of service",
            new Contact("John Doe", "", "myeaddress@company.com"),
            "License of API", "API license URL", Collections.emptyList());
    }
}

// http://localhost:8080/v2/api-docs     - Api

//   http://localhost:8080/swagger-ui.html    - proper swagger