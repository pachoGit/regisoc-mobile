package com.pacho.regisoc.ui.features.lineup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.LineupResponse
import com.pacho.regisoc.domain.usecase.GetLineupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LineupDetailUiState {
    data object Loading : LineupDetailUiState()
    data class Error(val message: String) : LineupDetailUiState()

    /**
     * [lineup] puede ser null cuando el club aún no registró su planilla
     * (el backend responde 404 y el repositorio lo traduce a Success(null)).
     */
    data class Success(val lineup: LineupResponse?) : LineupDetailUiState()
}

/**
 * ViewModel de solo lectura para ver una planilla ya registrada
 * (la mía o la de mi club rival) para un partido determinado.
 */
class LineupDetailViewModel(
    private val getLineupUseCase: GetLineupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LineupDetailUiState>(LineupDetailUiState.Loading)
    val uiState: StateFlow<LineupDetailUiState> = _uiState.asStateFlow()

    fun load(matchId: Long, clubId: Long) {
        viewModelScope.launch {
            _uiState.value = LineupDetailUiState.Loading
            when (val result = getLineupUseCase(matchId, clubId)) {
                is Result.Error -> _uiState.value = LineupDetailUiState.Error(result.message)
                is Result.Success -> _uiState.value = LineupDetailUiState.Success(result.data)
            }
        }
    }
}
