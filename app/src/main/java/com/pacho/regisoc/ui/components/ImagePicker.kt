package com.pacho.regisoc.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

data class ImagePickerResult(
    val bytes: ByteArray? = null,
    val fileName: String? = null,
    val error: String? = null
) {
    val isSuccess: Boolean get() = bytes != null && fileName != null
}

@Composable
fun rememberImagePickerLauncher(
    onResult: (ImagePickerResult) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onResult(ImagePickerResult(error = "Selección cancelada"))
            return@rememberLauncherForActivityResult
        }
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: ByteArray(0)
            inputStream?.close()
            val fileName = "photo_${System.currentTimeMillis()}.jpg"
            onResult(ImagePickerResult(bytes = bytes, fileName = fileName))
        } catch (e: Exception) {
            onResult(ImagePickerResult(error = "Error al leer imagen: ${e.message}"))
        }
    }

    return { launcher.launch("image/*") }
}
