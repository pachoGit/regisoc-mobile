package com.pacho.regisoc.data.mock

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.PhotoUploadDataSource
import kotlinx.coroutines.delay

class MockPhotoUploadDataSource : PhotoUploadDataSource {

    private var uploadCounter = 0

    override suspend fun uploadPhoto(imageBytes: ByteArray, fileName: String): Result<String> {
        return try {
            delay(1500L)
            uploadCounter++
            val fakeUrl = "https://storage.appregisoc.com/photos/${uploadCounter}_${fileName}"
            Result.Success(fakeUrl)
        } catch (e: Exception) {
            Result.Error("Error al subir foto: ${e.message}")
        }
    }
}
