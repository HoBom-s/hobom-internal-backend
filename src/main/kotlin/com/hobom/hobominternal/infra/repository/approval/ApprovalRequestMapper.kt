package com.hobom.hobominternal.infra.repository.approval

import com.hobom.hobominternal.domain.approval.model.ApprovalContextSnapshot
import com.hobom.hobominternal.domain.approval.model.ApprovalRequest
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestId
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStage
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestStatus
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTemplateId
import com.hobom.hobominternal.domain.approval.model.ApprovalRequestTenantId
import com.hobom.hobominternal.domain.approval.model.ApprovalResource
import com.hobom.hobominternal.domain.approval.model.ApprovalStageId
import com.hobom.hobominternal.domain.approval.model.ApprovalStageStatus
import com.hobom.hobominternal.domain.approval.model.ApprovalStep
import com.hobom.hobominternal.domain.approval.model.ApprovalStepId
import com.hobom.hobominternal.domain.approval.model.ApprovalStepMode
import com.hobom.hobominternal.domain.approval.model.ApprovalStepStatus
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.JSONB
import org.jooq.generated.enums.ApprovalStatus
import org.jooq.generated.enums.StageStatus
import org.jooq.generated.enums.StepMode
import org.jooq.generated.enums.StepStatus
import org.jooq.generated.tables.records.ApprovalRequestRecord
import org.jooq.generated.tables.references.APPROVAL_REQUEST
import org.jooq.generated.tables.references.APPROVAL_STAGE
import org.jooq.generated.tables.references.APPROVAL_STEP
import org.jooq.generated.tables.references.APPROVAL_STEP_APPROVER
import java.time.ZoneOffset

object ApprovalRequestMapper {
    fun rehydrateAggregate(dsl: DSLContext, root: ApprovalRequestRecord): ApprovalRequest {
        val approvalId = root.id!!
        val stageRecs = dsl.selectFrom(APPROVAL_STAGE)
            .where(APPROVAL_STAGE.APPROVAL_ID.eq(approvalId))
            .orderBy(APPROVAL_STAGE.STAGE_ORDER.asc())
            .fetch()
        val stepRecs = dsl.selectFrom(APPROVAL_STEP)
            .where(APPROVAL_STEP.STAGE_ID.`in`(stageRecs.map { it.id }))
            .fetch()
        val approverRecs = dsl.selectFrom(APPROVAL_STEP_APPROVER)
            .where(APPROVAL_STEP_APPROVER.STEP_ID.`in`(stepRecs.map { it.id }))
            .fetch()
            .groupBy { it.stepId }
        val domainStages = stageRecs.map { st ->
            val stepsForStage = stepRecs.filter { it.stageId == st.id }.map { sp ->
                val approverIds: Set<String> =
                    approverRecs[sp.id]?.mapNotNull { it.userId }?.toSet() ?: emptySet()
                ApprovalStep(
                    id = sp.id?.let { ApprovalStepId(it.toLong()) },
                    approverIds = approverIds,
                    status = ApprovalStepStatus.valueOf(sp.status.toString()),
                    decidedBy = sp.decidedBy,
                    decidedAt = sp.decidedAt?.toInstant(),
                    comment = sp.comment,
                )
            }.toMutableList()

            ApprovalRequestStage(
                id = st.id?.let { ApprovalStageId(it.toLong()) },
                order = st.stageOrder!!,
                mode = ApprovalStepMode.valueOf(st.mode.toString()),
                approvalStageStatus = ApprovalStageStatus.valueOf(st.status.toString()),
                decidedBy = st.decidedBy,
                decidedAt = st.decidedAt?.toInstant(),
                comment = st.comment,
                approvalRequestSteps = stepsForStage,
            )
        }

        return ApprovalRequest.rehydrate(
            id = approvalId,
            tenantId = ApprovalRequestTenantId(root.tenantId!!),
            requesterId = root.requesterId!!,
            title = root.title!!,
            status = ApprovalRequestStatus.valueOf(root.status.toString()),
            version = root.version ?: 1,
            resource = root.resourceType?.let {
                ApprovalResource(it, root.resourceId!!, root.resourceVer)
            },
            contextSnapshot = toSnapshot(root.contextSnapshot).toString(),
            contextHash = root.contextHash,
            templateId = ApprovalRequestTemplateId(root.templateId ?: ""),
            stages = domainStages,
        )
    }

    fun insertAggregate(dsl: DSLContext, agg: ApprovalRequest) {
        val inserted = dsl.insertInto(APPROVAL_REQUEST)
            .set(APPROVAL_REQUEST.TENANT_ID, agg.tenantId.value)
            .set(APPROVAL_REQUEST.REQUESTER_ID, agg.requesterId)
            .set(APPROVAL_REQUEST.TITLE, agg.title)
            .set(APPROVAL_REQUEST.STATUS, agg.status.toDb())
            .set(APPROVAL_REQUEST.VERSION, agg.version)
            .set(APPROVAL_REQUEST.RESOURCE_TYPE, agg.resource?.type)
            .set(APPROVAL_REQUEST.RESOURCE_ID, agg.resource?.id)
            .set(APPROVAL_REQUEST.RESOURCE_VER, agg.resource?.version)
            .set(APPROVAL_REQUEST.CONTEXT_SNAPSHOT, JSON.valueOf(agg.contextSnapshot.raw))
            .set(APPROVAL_REQUEST.CONTEXT_HASH, agg.contextHash)
            .set(APPROVAL_REQUEST.TEMPLATE_ID, agg.templateId?.value)
            .returning(APPROVAL_REQUEST.ID)
            .fetchOne() ?: error("Failed to insert approval_request")

        val approvalId = inserted.id!!.toLong()
        agg.id = ApprovalRequestId(approvalId)
        agg.stages.forEach { st ->
            val stRec = dsl.insertInto(APPROVAL_STAGE)
                .set(APPROVAL_STAGE.APPROVAL_ID, approvalId)
                .set(APPROVAL_STAGE.STAGE_ORDER, st.order)
                .set(APPROVAL_STAGE.MODE, st.mode.toDb())
                .set(APPROVAL_STAGE.STATUS, st.approvalStageStatus.toDb())
                .set(APPROVAL_STAGE.DECIDED_BY, st.decidedBy)
                .set(APPROVAL_STAGE.DECIDED_AT, st.decidedAt?.atOffset(ZoneOffset.UTC))
                .set(APPROVAL_STAGE.COMMENT, st.comment)
                .returning(APPROVAL_STAGE.ID)
                .fetchOne()!!

            val stageId = stRec.id!!.toLong()
            st.id = ApprovalStageId(stageId)
            st.approvalRequestSteps.forEach { sp ->
                val spRec = dsl.insertInto(APPROVAL_STEP)
                    .set(APPROVAL_STEP.STAGE_ID, stageId)
                    .set(APPROVAL_STEP.STATUS, sp.status.toDb())
                    .set(APPROVAL_STEP.DECIDED_BY, sp.decidedBy)
                    .set(APPROVAL_STEP.DECIDED_AT, sp.decidedAt?.atOffset(ZoneOffset.UTC))
                    .set(APPROVAL_STEP.COMMENT, sp.comment)
                    .returning(APPROVAL_STEP.ID)
                    .fetchOne()!!

                val stepId = spRec.id!!.toLong()
                sp.id = ApprovalStepId(stepId)
                if (sp.approverIds.isNotEmpty()) {
                    dsl.batch(
                        sp.approverIds.map { uid ->
                            dsl.insertInto(APPROVAL_STEP_APPROVER)
                                .set(APPROVAL_STEP_APPROVER.STEP_ID, stepId)
                                .set(APPROVAL_STEP_APPROVER.USER_ID, uid)
                        },
                    ).execute()
                }
            }
        }
    }

    private fun toSnapshot(raw: Any?): ApprovalContextSnapshot =
        when (raw) {
            null -> ApprovalContextSnapshot("{}")
            is JSONB -> ApprovalContextSnapshot(raw.data())
            is JSON -> ApprovalContextSnapshot(raw.data())
            is String -> ApprovalContextSnapshot(raw)
            else -> ApprovalContextSnapshot(raw.toString())
        }

    private fun ApprovalRequestStatus.toDb(): ApprovalStatus = ApprovalStatus.valueOf(this.name)

    private fun ApprovalStepStatus.toDb(): StepStatus = StepStatus.valueOf(this.name)

    private fun ApprovalStepMode.toDb(): StepMode = StepMode.valueOf(this.name)

    private fun ApprovalStageStatus.toDb(): StageStatus = StageStatus.valueOf(this.name)
}
