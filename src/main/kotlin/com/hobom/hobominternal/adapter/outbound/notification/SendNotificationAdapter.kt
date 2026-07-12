package com.hobom.hobominternal.adapter.outbound.notification

import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import com.hobom.hobominternal.infra.feign.hobom.HoBomBackendFeignClient
import com.hobom.hobominternal.infra.feign.hobom.dto.CreateNotificationRequest
import com.hobom.hobominternal.shared.logging.MaskUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SendNotificationAdapter(
    private val hobomBackendFeignClient: HoBomBackendFeignClient,
) : SendNotificationPort {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(request: NotificationRequest) {
        if (request.body.isNullOrBlank()) {
            log.info("Skipping notification: body is empty, recipient: {}, category: {}", MaskUtils.maskRecipient(request.recipient), request.category)
            return
        }
        log.info("Calling for-hobom-backend: POST /internal/notifications, recipient: {}, category: {}", MaskUtils.maskRecipient(request.recipient), request.category)
        hobomBackendFeignClient.createNotification(
            CreateNotificationRequest(
                category = request.category.value,
                recipient = request.recipient,
                title = request.title,
                body = request.body,
                senderId = request.senderId,
            ),
        )
        log.info("for-hobom-backend notification created successfully for recipient: {}", MaskUtils.maskRecipient(request.recipient))
    }
}
