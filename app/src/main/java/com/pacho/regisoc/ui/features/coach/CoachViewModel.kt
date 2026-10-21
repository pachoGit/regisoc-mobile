package com.pacho.regisoc.ui.features.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.DeleteCoachUseCase
import com.pacho.regisoc.domain.usecase.GetCoachesUseCase
import com.pacho.regisoc.domain.usecase.SaveCoachUseCase
import com.pacho.regisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.regisoc.ui.common.PhotoType
import com.pacho.regisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CoachUiState {
    data object Loading : CoachUiState()
    data class Success(val coaches: List<CoachResponse>) : CoachUiState()
    data class Error(val message: String) : CoachUiState()
}

class CoachViewModel(
    private val getCoachesUseCase: GetCoachesUseCase,
    private val saveCoachUseCase: SaveCoachUseCase,
    private val deleteCoachUseCase: DeleteCoachUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<CoachUiState>(CoachUiState.Loading)
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    val clubId: Long
        get() = sessionManager.clubId

    fun loadCoaches() {
        viewModelScope.launch {
            _uiState.value = CoachUiState.Loading
            when (val result = getCoachesUseCase(sessionManager.clubId)) {
                is Result.Error -> _uiState.value = CoachUiState.Error(result.message)
                is Result.Success -> _uiState.value = CoachUiState.Success(result.data)
            }
        }
    }

    fun saveCoach(formState: CoachFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = saveCoachUseCase(
                    id = formState.editingCoachId,
                    firstName = formState.firstName,
                    lastName = formState.lastName,
                    documentNumber = formState.documentNumber,
                    age = formState.age,
                    dateOfBirth = formState.dateOfBirth,
                    clubId = sessionManager.clubId,
                    photoUrl = formState.photoUrl.ifBlank { null },
                    documentFrontUrl = formState.documentFrontUrl.ifBlank { null },
                    documentBackUrl = formState.documentBackUrl.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit(
                            if (formState.isEditing) "Entrenador actualizado correctamente"
                            else "Entrenador registrado correctamente"
                        )
                        loadCoaches()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCoach(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deleteCoachUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Entrenador eliminado")
                        loadCoaches()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadPhoto(
        imageBytes: ByteArray,
        fileName: String,
        photoType: PhotoType,
        onStateUpdate: (PhotoPickerState) -> Unit
    ) {
        viewModelScope.launch {
            onStateUpdate(PhotoPickerState(isUploading = true))

            val result = uploadPhotoUseCase(imageBytes, fileName)
            when (result) {
                is Result.Error -> {
                    onStateUpdate(PhotoPickerState(error = result.message))
                    _snackBarMessage.emit(result.message)
                }
                is Result.Success -> {
                    onStateUpdate(PhotoPickerState(remoteUrl = result.data))
                    _snackBarMessage.emit("Foto subida correctamente")
                }
            }
        }
    }
}
