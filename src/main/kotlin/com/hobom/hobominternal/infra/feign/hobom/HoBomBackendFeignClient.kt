package com.hobom.hobominternal.infra.feign.hobom

import com.hobom.hobominternal.config.HoBomBackendFeignConfig
import com.hobom.hobominternal.infra.feign.hobom.dto.CreateNotificationRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "hobomBackendClient",
    url = "\${hobom-backend.url}",
    configuration = [HoBomBackendFeignConfig::class],
)
interface HoBomBackendFeignClient {
    @PostMapping("/hobom-system-backend/internal/notifications")
    fun createNotification(
        @RequestBody request: CreateNotificationRequest,
    )
}
