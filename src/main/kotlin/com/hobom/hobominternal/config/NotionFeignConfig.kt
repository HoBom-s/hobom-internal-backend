package com.hobom.hobominternal.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.RequestTemplate
import feign.codec.Decoder
import feign.codec.Encoder
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotionFeignConfig(
    @Value("\${notion.token}") private val notionToken: String,
    @Value("\${notion.version}") private val notionVersion: String,
    private val objectMapper: ObjectMapper,
) : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        requestTemplate.header("Authorization", notionToken)
        requestTemplate.header("Notion-Version", notionVersion)
        requestTemplate.header("Content-Type", "application/json")
    }

    @Bean
    fun feignEncoder(): Encoder = JacksonEncoder(objectMapper)

    @Bean
    fun feignDecoder(): Decoder = JacksonDecoder(objectMapper)
}
