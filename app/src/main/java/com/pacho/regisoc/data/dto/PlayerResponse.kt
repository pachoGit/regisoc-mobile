package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlayerPosition {
    @SerialName("GOALKEEPER") GOALKEEPER,
    @SerialName("DEFENDER") DEFENDER,
    @SerialName("MIDFIELDER") MIDFIELDER,
    @SerialName("FORWARD") FORWARD
}

@Serializable
data class PlayerResponse(
    val id: Long,
    @SerialName("clubId") val clubId: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("documentNumber") val documentNumber: String,
    val age: Int,
    @SerialName("dateOfBirth") val dateOfBirth: String,
    val position: PlayerPosition? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("documentFrontUrl") val documentFrontUrl: String? = null,
    @SerialName("documentBackUrl") val documentBackUrl: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)
