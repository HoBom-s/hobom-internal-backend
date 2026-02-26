package com.hobom.hobominternal.adapter.outbound.notification

import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import com.hobom.hobominternal.infra.feign.hobom.HoBomBackendFeignClient
import com.hobom.hobominternal.infra.feign.hobom.dto.CreateNotificationRequest
import org.springframework.stereotype.Component

@Component
class SendNotificationAdapter(
    private val hobomBackendFeignClient: HoBomBackendFeignClient,
) : SendNotificationPort {
    override fun send(request: NotificationRequest) {
        hobomBackendFeignClient.createNotification(
            CreateNotificationRequest(
                category = request.category.value,
                recipient = request.recipient,
                title = request.title,
                body = request.body,
                senderId = request.senderId,
            ),
        )
    }
}
