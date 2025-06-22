package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import java.time.Instant

data class HoBomLogSearchCriteria(
    val serviceType: ServiceType? = null,
    val httpMethod: HttpMethodType? = null,
    val statusCode: Int? = null,
    val startedAt: Instant? = null,
    val endedAt: Instant? = null,
)
