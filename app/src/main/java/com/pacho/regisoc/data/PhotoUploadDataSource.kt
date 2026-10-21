package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result

interface PhotoUploadDataSource {
    suspend fun uploadPhoto(imageBytes: ByteArray, fileName: String): Result<String>
}
