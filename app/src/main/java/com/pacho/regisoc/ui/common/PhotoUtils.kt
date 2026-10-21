package com.pacho.regisoc.ui.common

import com.pacho.regisoc.ui.components.ImagePickerResult
import com.pacho.regisoc.ui.components.PhotoPickerState

sealed class PhotoType {
    data object PLAYER : PhotoType()
    data object COACH : PhotoType()
    data object TRAINER : PhotoType()
    data object DNI_FRONT : PhotoType()
    data object DNI_BACK : PhotoType()
}

interface PhotoFormState {
    val photoState: PhotoPickerState
    val dniFrontPhotoState: PhotoPickerState
    val dniBackPhotoState: PhotoPickerState
}

fun handleImagePickerResult(
    result: ImagePickerResult,
    photoType: PhotoType?,
    onUploadPhoto: (ByteArray, String, PhotoType, (PhotoPickerState) -> Unit) -> Unit,
    onStateUpdate: (PhotoPickerState) -> Unit
) {
    if (result.isSuccess && photoType != null) {
        onUploadPhoto(result.bytes!!, result.fileName!!, photoType, onStateUpdate)
    } else if (result.error != null) {
        onStateUpdate(PhotoPickerState(error = result.error))
    }
}

inline fun <T : PhotoFormState> updateFormPhotoState(
    formState: T,
    photoType: PhotoType,
    newState: PhotoPickerState,
    copyWith: (photo: PhotoPickerState, dniFront: PhotoPickerState, dniBack: PhotoPickerState) -> T
): T = when (photoType) {
    PhotoType.PLAYER, PhotoType.COACH, PhotoType.TRAINER ->
        copyWith(newState, formState.dniFrontPhotoState, formState.dniBackPhotoState)

    PhotoType.DNI_FRONT ->
        copyWith(formState.photoState, newState, formState.dniBackPhotoState)

    PhotoType.DNI_BACK ->
        copyWith(formState.photoState, formState.dniFrontPhotoState, newState)
}
