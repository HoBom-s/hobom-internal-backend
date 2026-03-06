package com.hobom.hobominternal.infra.scheduler

import com.hobom.hobominternal.domain.log.port.inbound.DeleteOldHoBomLogUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class HoBomLogCleanupSchedulerTest {
    private val deleteOldHoBomLogUseCase = mockk<DeleteOldHoBomLogUseCase>()
    private val scheduler = HoBomLogCleanupScheduler(deleteOldHoBomLogUseCase)

    @Test
    fun `should invoke use case on cleanup`() {
        every { deleteOldHoBomLogUseCase.invoke() } returns 150

        scheduler.cleanup()

        verify(exactly = 1) { deleteOldHoBomLogUseCase.invoke() }
    }

    @Test
    fun `should not throw when no logs deleted`() {
        every { deleteOldHoBomLogUseCase.invoke() } returns 0

        scheduler.cleanup()

        verify(exactly = 1) { deleteOldHoBomLogUseCase.invoke() }
    }
}
