package com.hobom.hobominternal.domain.approval.model

import java.security.SecureRandom

@JvmInline
value class ApprovalRequestTenantId(val value: String)

object ApprovalRequestTenantIdGen {
    const val TENANT_DISPLAY_NAME = "HOBOM_APPROVAL_TENANT"

    private val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    private val RNG = SecureRandom()

    fun slugRand(displayName: String): ApprovalRequestTenantId {
        val slug = slugify(displayName)
        val rand = (1..8).map { ALPHABET[RNG.nextInt(ALPHABET.length)] }.joinToString("")
        return ApprovalRequestTenantId("tn-$slug-$rand")
    }

    private fun slugify(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .replace("[^a-z0-9]+".toRegex(), "-")
            .trim('-')
            .take(20)
}
