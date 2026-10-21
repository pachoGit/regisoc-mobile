package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventUpdateRequest(
    val title: String,
    val description: String? = null,
    val location: String? = null,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String? = null,
    val status: EventStatus = EventStatus.UPCOMING
)