package com.pinpoint.domain.model

data class UIGoogleUserInfo(
    val uid: String,
    val googleId: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
)
