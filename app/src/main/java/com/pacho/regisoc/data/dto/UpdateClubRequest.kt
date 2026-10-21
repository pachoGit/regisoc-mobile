package com.pacho.regisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClubRequest(
    val name: String,
    @SerialName("foundedYear") val foundedYear: Int? = null,
    @SerialName("crestUrl") val crestUrl: String? = null,
    val description: String? = null
)
