package com.hobom.hobominternal.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("HoBom Internal API")
                    .description("HoBom System Internal API")
                    .version("v1.0.0")
                    .contact(
                        Contact().apply {
                            name = "HoBom"
                            url = "https://github.com/hobom-s"
                        },
                    ),
            )
            .components(
                Components().addSecuritySchemes(
                    "ApiKey",
                    SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("X-API-Key"),
                ),
            )
            .addSecurityItem(SecurityRequirement().addList("ApiKey"))
    }
}
