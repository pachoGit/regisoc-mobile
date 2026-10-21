package com.pacho.regisoc.ui.features.physicaltrainer

import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.ui.common.PhotoFormState
import com.pacho.regisoc.ui.components.PhotoPickerState

data class PhysicalTrainerFormState(
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
    val editingId: Long? = null
) : PhotoFormState {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val documentFrontUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val documentBackUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromPhysicalTrainer(trainer: PhysicalTrainerResponse) = PhysicalTrainerFormState(
            firstName = trainer.firstName,
            lastName = trainer.lastName,
            documentNumber = trainer.documentNumber,
            age = trainer.age.toString(),
            dateOfBirth = trainer.dateOfBirth,
            photoState = PhotoPickerState(remoteUrl = trainer.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = trainer.documentFrontUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = trainer.documentBackUrl),
            isEditing = true,
            editingId = trainer.id
        )
    }
}
