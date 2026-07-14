package com.hobom.hobominternal.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager(
            NOTION_ARTICLES,
            NOTION_ARTICLE_BY_SLUG,
            NOTION_BLOCK,
        )
        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500),
        )
        return cacheManager
    }

    companion object {
        const val NOTION_ARTICLES = "notion-articles"
        const val NOTION_ARTICLE_BY_SLUG = "notion-article-by-slug"
        const val NOTION_BLOCK = "notion-block"
    }
}
