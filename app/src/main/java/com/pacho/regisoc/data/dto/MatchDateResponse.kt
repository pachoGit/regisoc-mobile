package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MatchDateStatus {
    @SerialName("UPCOMING") UPCOMING,
    @SerialName("ONGOING") ONGOING,
    @SerialName("FINISHED") FINISHED,
    @SerialName("CANCELED") CANCELED
}

@Serializable
data class MatchDateResponse(
    val id: Long,
    val date: String,
    @SerialName("startTime") val startTime: String? = null,
    val location: String? = null,
    val name: String = "",
    val status: MatchDateStatus = MatchDateStatus.UPCOMING,
    @SerialName("match") val match: MatchResponse? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
) {
    val hasMatch: Boolean
        get() = match != null

    val homeClub: ClubResponse?
        get() = match?.homeClub

    val awayClub: ClubResponse?
        get() = match?.awayClub

    val homeClubName: String
        get() = homeClub?.name ?: "Mi Club"

    val awayClubName: String
        get() = awayClub?.name ?: "Club Rival"
}