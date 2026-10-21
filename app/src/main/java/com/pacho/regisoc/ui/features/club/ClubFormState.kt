package com.pacho.regisoc.ui.features.club

import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.ui.components.PhotoPickerState

data class ClubFormState(
    val name: String = "",
    val foundedYear: String = "",
    val description: String = "",
    val crestPhotoState: PhotoPickerState = PhotoPickerState(),
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingClubId: Long? = null
) {
    val crestUrl: String get() = crestPhotoState.remoteUrl ?: ""

    companion object {
        fun fromClub(club: ClubResponse) = ClubFormState(
            name = club.name,
            foundedYear = club.foundedYear?.toString() ?: "",
            description = club.description ?: "",
            crestPhotoState = PhotoPickerState(remoteUrl = club.crestUrl),
            isEditing = true,
            editingClubId = club.id
        )
    }
}
