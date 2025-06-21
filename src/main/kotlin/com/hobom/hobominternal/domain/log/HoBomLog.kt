package com.hobom.hobominternal.domain.log

import com.hobom.hobominternal.domain.base.BaseEntity
import com.hobom.hobominternal.domain.service.HttpMethodType
import com.hobom.hobominternal.domain.service.ServiceType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "logs")
data class HoBomLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Enumerated(EnumType.STRING)
    val serviceType: ServiceType,

    @Enumerated(EnumType.STRING)
    val level: HoBomLogLevel,

    @Column(name = "trace_id")
    val traceId: String,

    val message: String,

    @Enumerated(EnumType.STRING)
    val httpMethod: HttpMethodType,

    val path: String?,

    @Column(name = "status_code")
    val statusCode: Int,

    val host: String,

    val userId: String,

    @Column(columnDefinition = "json")
    val payload: String?,

    val timestamp: Instant,
) : BaseEntity()
