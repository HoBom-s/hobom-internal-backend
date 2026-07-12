package com.hobom.hobominternal.application.command.space

data class ProcessSpaceEventCommand(
    val entityType: String,
    val action: String,
    val spaceKey: String,
    val pageId: Long,
    val title: String,
    val actorId: String?,
)
