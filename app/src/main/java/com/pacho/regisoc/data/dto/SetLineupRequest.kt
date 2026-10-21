package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetLineupRequest(
    @SerialName("matchId") val matchId: Long,
    @SerialName("clubId") val clubId: Long,
    @SerialName("playerIds") val playerIds: List<Long> = emptyList(),
    @SerialName("coachId") val coachId: Long? = null,
    @SerialName("physicalTrainerId") val physicalTrainerId: Long? = null
)
