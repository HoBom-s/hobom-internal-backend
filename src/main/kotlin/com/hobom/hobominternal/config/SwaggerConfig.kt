package com.hobom.hobominternal.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
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
    }
}
