package com.hobom.hobominternal.infra.feign.notion.client

import com.hobom.hobominternal.config.NotionFeignConfig
import com.hobom.hobominternal.infra.feign.notion.dto.BlockChildrenResponse
import com.hobom.hobominternal.infra.feign.notion.dto.DatabaseQueryResponse
import com.hobom.hobominternal.infra.feign.notion.dto.NotionPage
import com.hobom.hobominternal.infra.feign.notion.dto.NotionQueryRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

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

    @GetMapping("/pages/{pageId}")
    fun getPage(
        @PathVariable pageId: String,
    ): NotionPage

    @GetMapping("/blocks/{pageId}/children")
    fun getBlockChildren(
        @PathVariable pageId: String,
    ): BlockChildrenResponse

    @GetMapping("/blocks/{blockId}/children")
    fun getBlockChildren(
        @PathVariable blockId: String,
        @RequestParam("start_cursor", required = false) startCursor: String? = null,
        @RequestParam("page_size", required = false) pageSize: Int? = 100,
    ): BlockChildrenResponse
}
