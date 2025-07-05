package com.hobom.hobominternal.infra.feign.notion.client

import com.hobom.hobominternal.config.NotionFeignConfig
import com.hobom.hobominternal.infra.feign.notion.dto.BlockChildrenResponse
import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "notionClient",
    url = "https://api.notion.com/v1",
    configuration = [NotionFeignConfig::class],
)
interface NotionFeignClient {
    @PostMapping("/databases/{databaseId}/query")
    fun getDatabase(
        @PathVariable databaseId: String,
        @RequestBody body: NotionQueryRequest,
    ): DatabaseQueryResponse

    @GetMapping("/blocks/{pageId}/children")
    fun getBlockChildren(
        @PathVariable pageId: String,
    ): BlockChildrenResponse
}
