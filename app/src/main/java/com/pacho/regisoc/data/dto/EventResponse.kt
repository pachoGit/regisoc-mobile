package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventStatus {
    @SerialName("UPCOMING") UPCOMING,
    @SerialName("ONGOING") ONGOING,
    @SerialName("FINISHED") FINISHED
}

@Serializable
data class EventResponse(
    val id: Long,
    // @SerialName("clubId") val clubId: Long,
    val name: String,
    val description: String? = null,
    val location: String? = null,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String? = null,
    val status: EventStatus = EventStatus.UPCOMING,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)