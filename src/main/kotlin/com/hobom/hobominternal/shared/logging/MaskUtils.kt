package com.hobom.hobominternal.shared.logging

object MaskUtils {
    fun maskRecipient(recipient: String): String {
        val atIndex = recipient.indexOf('@')
        if (atIndex <= 1) return "***"
        return "${recipient.take(2)}***${recipient.substring(atIndex)}"
    }
}
