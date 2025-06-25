//package com.hobom.hobominternal.infra.kafka.producer.log
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.hobom.hobominternal.application.command.log.SaveLogCommand
//import com.hobom.hobominternal.domain.log.HoBomLogLevel
//import com.hobom.hobominternal.domain.log.HttpMethodType
//import com.hobom.hobominternal.domain.log.ServiceType
//import com.hobom.hobominternal.shared.kafka.KafkaTopics
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.kafka.core.KafkaTemplate
//import org.springframework.test.context.TestConstructor
//
//@SpringBootTest
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//class KafkaHoBomLogProducerTest(
//    private val kafkaTemplate: KafkaTemplate<String, String>,
//) {
//    @Test
//    fun `Kafka send HoBomLogs`() {
//        val logCommand = SaveLogCommand(
//            serviceType = ServiceType.HOBOM_BACKEND,
//            level = HoBomLogLevel.INFO,
//            traceId = "trace-123",
//            message = "Test log",
//            httpMethod = HttpMethodType.POST,
//            path = "/test",
//            statusCode = 200,
//            host = "hobom-internal",
//            userId = "user-1",
//            payload = mapOf("key" to "value"),
//        )
//        val json = jacksonObjectMapper().writeValueAsString(logCommand)
//
//        kafkaTemplate.send(KafkaTopics.HoBomLogs.TOPIC, json)
//        kafkaTemplate.flush()
//
//        // if you want to check consume function
//        Thread.sleep(5000)
//    }
//}
