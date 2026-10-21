package com.pacho.regisoc.ui.features.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.GetMatchDatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MatchDateUiState {
    data object Loading : MatchDateUiState()
    data class Success(val matchDates: List<MatchDateResponse>) : MatchDateUiState()
    data class Error(val message: String) : MatchDateUiState()
}

class MatchDateViewModel(
    private val getMatchDatesUseCase: GetMatchDatesUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MatchDateUiState>(MatchDateUiState.Loading)
    val uiState: StateFlow<MatchDateUiState> = _uiState.asStateFlow()

    val clubId: Long
        get() = sessionManager.clubId

    fun loadMatchDates(eventId: Long) {
        viewModelScope.launch {
            _uiState.value = MatchDateUiState.Loading
            when (val result = getMatchDatesUseCase(eventId, sessionManager.clubId)) {
                is Result.Error -> _uiState.value = MatchDateUiState.Error(result.message)
                is Result.Success -> _uiState.value = MatchDateUiState.Success(result.data)
            }
        }
    }
}
