package com.hobom.hobominternal.domain.notification.port.outbound

import com.hobom.hobominternal.domain.notification.model.NotificationRequest

interface SendNotificationPort {
    fun send(request: NotificationRequest)
}
