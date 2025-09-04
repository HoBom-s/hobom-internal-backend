package com.hobom.hobominternal.domain.approval.port.inbound

import com.hobom.hobominternal.application.command.approval.CreateApprovalRequestCommand

interface CreateApprovalRequestUseCase {
    fun invoke(command: CreateApprovalRequestCommand, idempotencyKey: String)
}
