package com.pacho.regisoc.ui.features.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.regisoc.domain.usecase.GetPlayersUseCase
import com.pacho.regisoc.domain.usecase.SavePlayerUseCase
import com.pacho.regisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.regisoc.ui.common.PhotoType
import com.pacho.regisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data class Success(val players: List<PlayerResponse>) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}

class PlayerViewModel(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val savePlayerUseCase: SavePlayerUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    val clubId: Long
        get() = sessionManager.clubId

    fun loadPlayers() {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            when (val result = getPlayersUseCase(sessionManager.clubId)) {
                is Result.Error -> _uiState.value = PlayerUiState.Error(result.message)
                is Result.Success -> _uiState.value = PlayerUiState.Success(result.data)
            }
        }
    }

    fun savePlayer(formState: PlayerFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = savePlayerUseCase(
                    id = formState.editingPlayerId,
                    firstName = formState.firstName,
                    lastName = formState.lastName,
                    documentNumber = formState.documentNumber,
                    age = formState.age,
                    dateOfBirth = formState.dateOfBirth,
                    clubId = sessionManager.clubId,
                    position = formState.position,
                    photoUrl = formState.photoUrl.ifBlank { null },
                    documentFrontUrl = formState.documentFrontUrl.ifBlank { null },
                    documentBackUrl = formState.documentBackUrl.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit(
                            if (formState.isEditing) "Jugador actualizado correctamente"
                            else "Jugador registrado correctamente"
                        )
                        loadPlayers()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deletePlayerUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Jugador eliminado")
                        loadPlayers()
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
