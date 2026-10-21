package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MatchStatus {
    @SerialName("UPCOMING") UPCOMING,
    @SerialName("ONGOING") ONGOING,
    @SerialName("FINISHED") FINISHED,
    @SerialName("CANCELED") CANCELED
}

@Serializable
data class MatchResponse(
    val id: Long,
    @SerialName("homeClub") val homeClub: ClubResponse? = null,
    @SerialName("awayClub") val awayClub: ClubResponse? = null,
    @SerialName("scheduledTime") val scheduledTime: String? = null,
    val status: MatchStatus = MatchStatus.UPCOMING,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)