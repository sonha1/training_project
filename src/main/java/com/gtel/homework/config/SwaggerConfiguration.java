package com.gtel.homework.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
                .addSchemas("ApiErrorResponse", new ObjectSchema()
                        .addProperty("status", new IntegerSchema())
                        .addProperty("code", new IntegerSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("fieldErrors", new ArraySchema()
                                .items(new Schema<ArraySchema>()
                                        .$ref("ApiErrorResponse"))))
                .addSchemas("ApiFieldError", new ObjectSchema()
                        .addProperty("code", new StringSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("property", new StringSchema())
                        .addProperty("path", new StringSchema())
                        .addProperty("rejectedValue", new ObjectSchema())
                ));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("*/*", new MediaType().schema(new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }

}
