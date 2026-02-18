package com.hobom.hobominternal.shared.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JsonUtil {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        fun parseJson(json: String?): Map<String, Any>? {
            if (json == null) return null
            return objectMapper.readValue(json)
        }
    }
}
