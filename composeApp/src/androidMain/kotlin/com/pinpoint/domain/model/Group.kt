package com.pinpoint.domain.model

data class Group(
    val id: String = "",
    val name: String = "",
    val createdBy: String = "",
    val createdByName: String = "",
    val createdAt: Long = 0L,
    val memberCount: Int = 0,
    val members: Map<String, GroupMember> = emptyMap()
)

data class GroupMember(
    val uid: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val joinedAt: Long = 0L
)
