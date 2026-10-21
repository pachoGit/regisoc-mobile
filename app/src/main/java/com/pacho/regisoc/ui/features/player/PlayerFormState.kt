package com.pacho.regisoc.ui.features.player

import com.pacho.regisoc.data.dto.PlayerPosition
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.ui.common.PhotoFormState
import com.pacho.regisoc.ui.components.PhotoPickerState

data class PlayerFormState(
    override val photoState: PhotoPickerState = PhotoPickerState(),
    override val dniFrontPhotoState: PhotoPickerState = PhotoPickerState(),
    override val dniBackPhotoState: PhotoPickerState = PhotoPickerState(),
    val firstName: String = "",
    val lastName: String = "",
    val documentNumber: String = "",
    val age: String = "",
    val dateOfBirth: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingPlayerId: Long? = null,
    val position: PlayerPosition? = null
) : PhotoFormState {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val documentFrontUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val documentBackUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromPlayer(player: PlayerResponse) = PlayerFormState(
            firstName = player.firstName,
            lastName = player.lastName,
            documentNumber = player.documentNumber,
            age = player.age.toString(),
            dateOfBirth = player.dateOfBirth,
            photoState = PhotoPickerState(remoteUrl = player.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = player.documentFrontUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = player.documentBackUrl),
            isEditing = true,
            editingPlayerId = player.id,
            position = player.position
        )
    }
}
