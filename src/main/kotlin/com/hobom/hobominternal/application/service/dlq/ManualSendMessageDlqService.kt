package com.hobom.hobominternal.application.service.dlq

import com.hobom.hobominternal.application.command.dlq.ManualSendMessageDlqCommand
import com.hobom.hobominternal.application.command.message.DeliverHoBomMessageCommand
import com.hobom.hobominternal.application.service.message.strategy.MessageSenderStrategy
import com.hobom.hobominternal.domain.dlq.DlqMessage
import com.hobom.hobominternal.domain.dlq.DlqMessageCreateRequest
import com.hobom.hobominternal.domain.dlq.DlqMessageId
import com.hobom.hobominternal.domain.message.MessageType
import com.hobom.hobominternal.port.inbound.dlq.ManualSendMessageDlqUseCase
import com.hobom.hobominternal.port.outbound.dlq.DlqMessagePersistencePort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManualSendMessageDlqService(
    private val strategies: List<MessageSenderStrategy>,
    private val dlqMessagePersistencePort: DlqMessagePersistencePort,
) : ManualSendMessageDlqUseCase {
    // Mail 혹은 Push는 Transaction 밖에서 실행되므로,
    // Mail, Push 전송 이후에, 오류가 발생하여 Transaction 어노테이션에 의해
    // DB 롤백이 되어버리면 올바르게 전송 이후, 데이터가 맞지 않는 정합성 문제가 발생할 수 있기 때문에,
    // Transaction 분리를 수행하도록 한다.
    override fun invoke(command: ManualSendMessageDlqCommand) {
        val dlq = loadEntity(command.id)
        val updatedDLQ = runCatching {
            send(command)
            dlq.markAsSuccess()
        }.getOrElse { exception ->
            dlq.markAsFailed(exception.message ?: "Unknown DLQ failure")
        }

        upsertTransactional(updatedDLQ)
    }

    @Transactional
    fun upsertTransactional(dlq: DlqMessage) {
        upsert(dlq.id, dlq.toCreateRequest())
    }

    private fun loadEntity(id: DlqMessageId): DlqMessage = dlqMessagePersistencePort
        .load(id)

    private fun upsert(id: DlqMessageId, request: DlqMessageCreateRequest) = dlqMessagePersistencePort
        .upsert(id, request)

    private fun send(command: ManualSendMessageDlqCommand) {
        val strategy = strategies
            .firstOrNull { it.supports(command.messageType) }
            ?: error("No strategy found for MAIL_MESSAGE")

        strategy.send(command.toMailCommand())
    }

    private fun ManualSendMessageDlqCommand.toMailCommand(): DeliverHoBomMessageCommand = DeliverHoBomMessageCommand(
        type = MessageType.MAIL_MESSAGE,
        title = title,
        body = body,
        recipient = recipient,
        senderId = senderId,
        sentAt = sentAt,
    )
}
