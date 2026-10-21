package com.pacho.regisoc.ui.features.physicaltrainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.DeletePhysicalTrainerUseCase
import com.pacho.regisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.regisoc.domain.usecase.SavePhysicalTrainerUseCase
import com.pacho.regisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.regisoc.ui.common.PhotoType
import com.pacho.regisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PhysicalTrainerUiState {
    data object Loading : PhysicalTrainerUiState()
    data class Success(val trainers: List<PhysicalTrainerResponse>) : PhysicalTrainerUiState()
    data class Error(val message: String) : PhysicalTrainerUiState()
}

class PhysicalTrainerViewModel(
    private val getPhysicalTrainersUseCase: GetPhysicalTrainersUseCase,
    private val savePhysicalTrainerUseCase: SavePhysicalTrainerUseCase,
    private val deletePhysicalTrainerUseCase: DeletePhysicalTrainerUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhysicalTrainerUiState>(PhysicalTrainerUiState.Loading)
    val uiState: StateFlow<PhysicalTrainerUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    val clubId: Long
        get() = sessionManager.clubId

    fun loadPhysicalTrainers() {
        viewModelScope.launch {
            _uiState.value = PhysicalTrainerUiState.Loading
            when(val result = getPhysicalTrainersUseCase(sessionManager.clubId)) {
                is Result.Error -> _uiState.value = PhysicalTrainerUiState.Error(result.message)
                is Result.Success -> _uiState.value = PhysicalTrainerUiState.Success(result.data)
            }
        }
    }

    fun savePhysicalTrainer(formState: PhysicalTrainerFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = savePhysicalTrainerUseCase(
                    id = formState.editingId,
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
                            if (formState.isEditing) "Preparador físico actualizado correctamente"
                            else "Preparador físico registrado correctamente"
                        )
                        loadPhysicalTrainers()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePhysicalTrainer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deletePhysicalTrainerUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Preparador físico eliminado")
                        loadPhysicalTrainers()
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
