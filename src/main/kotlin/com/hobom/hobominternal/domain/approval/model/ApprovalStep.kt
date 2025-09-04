package com.hobom.hobominternal.domain.approval.model

import java.time.Instant

class ApprovalStep(
    var id: ApprovalStepId? = null,
    val approverIds: Set<String>,
    var status: ApprovalStepStatus,
    var decidedBy: String? = null,
    var decidedAt: Instant? = null,
    var comment: String? = null,
)
