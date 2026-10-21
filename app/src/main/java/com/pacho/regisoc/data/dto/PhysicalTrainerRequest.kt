package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhysicalTrainerRequest(
    @SerialName("clubId") val clubId: Long,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("documentNumber") val documentNumber: String,
    val age: Int,
    @SerialName("dateOfBirth") val dateOfBirth: String,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("documentFrontUrl") val documentFrontUrl: String? = null,
    @SerialName("documentBackUrl") val documentBackUrl: String? = null
)
