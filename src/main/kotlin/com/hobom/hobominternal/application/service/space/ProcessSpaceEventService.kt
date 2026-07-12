package com.hobom.hobominternal.application.service.space

import com.hobom.hobominternal.application.command.space.ProcessSpaceEventCommand
import com.hobom.hobominternal.domain.notification.model.NotificationCategory
import com.hobom.hobominternal.domain.notification.model.NotificationRequest
import com.hobom.hobominternal.domain.notification.port.outbound.SendNotificationPort
import com.hobom.hobominternal.domain.space.port.inbound.ProcessSpaceEventUseCase
import com.hobom.hobominternal.infra.feign.hobom.HoBomBackendFeignClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProcessSpaceEventService(
    private val sendNotificationPort: SendNotificationPort,
    private val hobomBackendFeignClient: HoBomBackendFeignClient,
) : ProcessSpaceEventUseCase {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun invoke(command: ProcessSpaceEventCommand) {
        log.info("space event: {} {} {}", command.entityType, command.action, command.spaceKey)

        if (!command.actorId.isNullOrBlank()) {
            val userInfo = hobomBackendFeignClient.getUserInfoByNickname(command.actorId)

            sendNotificationPort.send(
                NotificationRequest(
                    category = NotificationCategory.SYSTEM,
                    recipient = command.actorId,
                    title = "[${command.spaceKey}] ${command.entityType} ${command.action}",
                    body = command.title,
                    senderId = userInfo.items.id,
                ),
            )
        }
    }
}
