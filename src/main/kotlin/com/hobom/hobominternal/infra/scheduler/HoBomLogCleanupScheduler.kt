package com.hobom.hobominternal.infra.scheduler

import com.hobom.hobominternal.domain.log.port.inbound.DeleteOldHoBomLogUseCase
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HoBomLogCleanupScheduler(
    private val deleteOldHoBomLogUseCase: DeleteOldHoBomLogUseCase,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 9 * * *")
    fun cleanup() {
        val totalDeleted = deleteOldHoBomLogUseCase.invoke()

        if (totalDeleted > 0) {
            log.info("Cleaned up {} old log entries (older than 30 days)", totalDeleted)
        }
    }
}
