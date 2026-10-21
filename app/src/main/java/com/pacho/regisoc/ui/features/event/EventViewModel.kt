package com.pacho.regisoc.ui.features.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.CreateEventUseCase
import com.pacho.regisoc.domain.usecase.DeleteEventUseCase
import com.pacho.regisoc.domain.usecase.GetEventsUseCase
import com.pacho.regisoc.domain.usecase.UpdateEventUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class EventUiState {
    data object Loading : EventUiState()
    data class Success(val events: List<EventResponse>) : EventUiState()
    data class Error(val message: String) : EventUiState()
}

class EventViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    val clubId: Long
        get() = sessionManager.clubId

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            when (val result = getEventsUseCase(sessionManager.clubId)) {
                is Result.Error -> _uiState.value = EventUiState.Error(result.message)
                is Result.Success -> _uiState.value = EventUiState.Success(result.data)
            }
        }
    }

    fun createEvent(formState: EventFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = createEventUseCase(
                    clubId = sessionManager.clubId,
                    title = formState.title,
                    description = formState.description.ifBlank { null },
                    location = formState.location.ifBlank { null },
                    startDate = formState.startDate,
                    endDate = formState.endDate.ifBlank { null },
                    status = formState.status
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Evento registrado correctamente")
                        loadEvents()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEvent(formState: EventFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val id = formState.editingEventId ?: return@launch
                val result = updateEventUseCase(
                    id = id,
                    title = formState.title,
                    description = formState.description.ifBlank { null },
                    location = formState.location.ifBlank { null },
                    startDate = formState.startDate,
                    endDate = formState.endDate.ifBlank { null },
                    status = formState.status
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Evento actualizado correctamente")
                        loadEvents()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deleteEventUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Evento eliminado")
                        loadEvents()
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
