package com.pacho.regisoc.ui.features.club

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.domain.usecase.CreateClubUseCase
import com.pacho.regisoc.domain.usecase.DeleteClubUseCase
import com.pacho.regisoc.domain.usecase.GetClubsUseCase
import com.pacho.regisoc.domain.usecase.UpdateClubUseCase
import com.pacho.regisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.regisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ClubUiState {
    data object Loading : ClubUiState()
    data class Success(val clubs: List<ClubResponse>) : ClubUiState()
    data class Error(val message: String) : ClubUiState()
}

class ClubViewModel(
    private val getClubsUseCase: GetClubsUseCase,
    private val createClubUseCase: CreateClubUseCase,
    private val updateClubUseCase: UpdateClubUseCase,
    private val deleteClubUseCase: DeleteClubUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClubUiState>(ClubUiState.Loading)
    val uiState: StateFlow<ClubUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    init {
        loadClubs()
    }

    private fun loadClubs() {
        viewModelScope.launch {
            getClubsUseCase()
                .onStart { _uiState.value = ClubUiState.Loading }
                .catch { e -> _uiState.value = ClubUiState.Error(e.message ?: "Error desconocido") }
                .collect { list ->
                    _uiState.value = ClubUiState.Success(list)
                }
        }
    }

    fun createClub(formState: ClubFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = createClubUseCase(
                    name = formState.name,
                    foundedYear = formState.foundedYear.toIntOrNull(),
                    crestUrl = formState.crestUrl.ifBlank { null },
                    description = formState.description.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit("Club creado correctamente")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateClub(formState: ClubFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val id = formState.editingClubId ?: return@launch
                val result = updateClubUseCase(
                    id = id,
                    name = formState.name,
                    foundedYear = formState.foundedYear.toIntOrNull(),
                    crestUrl = formState.crestUrl.ifBlank { null },
                    description = formState.description.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit("Club actualizado correctamente")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteClub(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deleteClubUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit("Club eliminado")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadCrestPhoto(
        imageBytes: ByteArray,
        fileName: String,
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
                    _snackBarMessage.emit("Escudo subido correctamente")
                }
            }
        }
    }
}
