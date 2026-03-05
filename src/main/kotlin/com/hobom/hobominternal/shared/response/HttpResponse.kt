package com.hobom.hobominternal.shared.response

import java.time.Instant

data class HttpResponse<T>(
    val success: Boolean,
    val message: String,
    val timestamp: Instant,
    val items: T,
) {
    companion object {
        fun <T> success(items: T, message: String = "OK"): HttpResponse<T> =
            HttpResponse(success = true, message = message, timestamp = Instant.now(), items = items)

        fun error(message: String): HttpResponse<Nothing?> =
            HttpResponse(success = false, message = message, timestamp = Instant.now(), items = null)
    }
}
