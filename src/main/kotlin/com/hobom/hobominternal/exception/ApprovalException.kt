package com.hobom.hobominternal.exception

sealed class ApprovalException(msg: String) : RuntimeException(msg) {
    class NotInReview : ApprovalException("Approval is not in review state")

    class NoPendingStage : ApprovalException("No pending stage")

    class AlreadyDecided : ApprovalException("Already decided")

    class NotFoundException : ApprovalException("Not Found Exception")
}
