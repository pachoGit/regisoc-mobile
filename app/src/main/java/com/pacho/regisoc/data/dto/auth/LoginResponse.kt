package com.pacho.regisoc.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val role: String,
    val clubId: Long?
)
