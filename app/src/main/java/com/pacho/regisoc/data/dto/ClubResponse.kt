package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubResponse(
    val id: Long,
    val name: String,
    @SerialName("foundedYear") val foundedYear: Int? = null,
    @SerialName("crestUrl") val crestUrl: String? = null,
    val description: String? = null,
    @SerialName("createdBy") val createdBy: String = "",
    @SerialName("isActive") val isActive: Boolean = true,
    @SerialName("createdAt") val createdAt: String = "",
    @SerialName("updatedAt") val updatedAt: String = ""
)
