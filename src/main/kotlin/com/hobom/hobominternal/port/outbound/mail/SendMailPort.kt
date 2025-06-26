package com.hobom.hobominternal.port.outbound.mail

import com.hobom.hobominternal.domain.mail.MailRequest

interface SendMailPort {
    fun send(request: MailRequest)
}
