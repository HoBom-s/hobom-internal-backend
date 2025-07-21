package com.hobom.hobominternal.adapter.outbound.log

import com.hobom.hobominternal.domain.log.model.HoBomLog
import com.hobom.hobominternal.domain.log.model.HoBomLogJdbcRepository
import com.hobom.hobominternal.domain.log.model.HoBomLogLevel
import com.hobom.hobominternal.domain.log.model.HttpMethodType
import com.hobom.hobominternal.domain.log.model.ServiceType
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Instant

class SaveHoBomLogPersistenceAdapterTest {

    private val mockRepository: HoBomLogJdbcRepository = mockk(relaxed = true)
    private val adapter = SaveHoBomLogPersistenceAdapter(mockRepository)

    @Test
    fun `should call repository batchInsert in SaveHoBomLogPersistenceAdapter`() {
        val logs = listOf(
            HoBomLog(
                serviceType = ServiceType.HOBOM_BACKEND,
                level = HoBomLogLevel.INFO,
                traceId = "abc-123",
                message = "Test Message",
                httpMethod = HttpMethodType.POST,
                path = "/test",
                statusCode = 200,
                host = "localhost",
                userId = "user-1",
                payload = mapOf("key" to "value"),
                timestamp = Instant.now(),
            ),
        )

        adapter.saveAll(logs)

        verify(exactly = 1) {
            mockRepository.batchInsert(logs)
        }
    }
}
