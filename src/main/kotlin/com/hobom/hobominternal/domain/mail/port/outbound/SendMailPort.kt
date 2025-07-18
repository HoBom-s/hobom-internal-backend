package com.hobom.hobominternal.domain.mail.port.outbound

import com.hobom.hobominternal.domain.mail.model.MailRequest

interface SendMailPort {
    fun send(request: MailRequest)
}
