package com.hobom.hobominternal.infra.feign.hobom

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.hobom.hobominternal.config.HoBomBackendFeignConfig
import com.hobom.hobominternal.infra.feign.hobom.dto.CreateNotificationRequest
import com.hobom.hobominternal.shared.response.HttpResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/hobom-system-backend/internal/users/{nickname}")
    fun getUserInfoByNickname(@PathVariable("nickname") nickname: String): HttpResponse<UserInfo>

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class UserInfo(
        val id: String,
        val username: String,
        val email: String,
        val nickname: String,
        val friends: List<String>,
    )
}
