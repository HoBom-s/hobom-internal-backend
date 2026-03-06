package com.hobom.hobominternal.application.service.log

import com.hobom.hobominternal.domain.log.port.outbound.DeleteOldHoBomLogPersistencePort
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DeleteOldHoBomLogServiceTest {
    private val persistencePort = mockk<DeleteOldHoBomLogPersistencePort>()
    private val service = DeleteOldHoBomLogService(persistencePort)

    @Test
    fun `should delete in batches until fewer than batch size returned`() {
        every { persistencePort.deleteBatch(any<LocalDateTime>(), 100) } returnsMany listOf(100, 100, 42)

        val totalDeleted = service.invoke()

        totalDeleted shouldBe 242
        verify(exactly = 3) { persistencePort.deleteBatch(any<LocalDateTime>(), 100) }
    }

    @Test
    fun `should stop after single batch when fewer than 100 deleted`() {
        every { persistencePort.deleteBatch(any<LocalDateTime>(), 100) } returns 30

        val totalDeleted = service.invoke()

        totalDeleted shouldBe 30
        verify(exactly = 1) { persistencePort.deleteBatch(any<LocalDateTime>(), 100) }
    }

    @Test
    fun `should return 0 when no old logs exist`() {
        every { persistencePort.deleteBatch(any<LocalDateTime>(), 100) } returns 0

        val totalDeleted = service.invoke()

        totalDeleted shouldBe 0
        verify(exactly = 1) { persistencePort.deleteBatch(any<LocalDateTime>(), 100) }
    }

    @Test
    fun `should stop exactly when batch returns full batch size then zero`() {
        every { persistencePort.deleteBatch(any<LocalDateTime>(), 100) } returnsMany listOf(100, 0)

        val totalDeleted = service.invoke()

        totalDeleted shouldBe 100
        verify(exactly = 2) { persistencePort.deleteBatch(any<LocalDateTime>(), 100) }
    }
}
