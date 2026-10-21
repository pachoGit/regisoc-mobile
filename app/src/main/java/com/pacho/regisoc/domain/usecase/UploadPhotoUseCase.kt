package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.PhotoUploadDataSource

class UploadPhotoUseCase(
    private val photoUploadDataSource: PhotoUploadDataSource
) {
    suspend operator fun invoke(imageBytes: ByteArray, fileName: String): Result<String> {
        if (imageBytes.isEmpty()) {
            return Result.Error("No se seleccionó ninguna imagen")
        }
        return photoUploadDataSource.uploadPhoto(imageBytes, fileName)
    }
}
