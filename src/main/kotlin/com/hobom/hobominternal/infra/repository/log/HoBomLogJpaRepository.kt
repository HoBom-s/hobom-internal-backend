package com.hobom.hobominternal.infra.repository.log

import com.hobom.hobominternal.domain.log.HoBomLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HoBomLogJpaRepository : JpaRepository<HoBomLog, Long>
