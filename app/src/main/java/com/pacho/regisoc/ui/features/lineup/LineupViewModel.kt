package com.pacho.regisoc.ui.features.lineup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.usecase.CloseLineupUseCase
import com.pacho.regisoc.domain.usecase.GetCoachesUseCase
import com.pacho.regisoc.domain.usecase.GetLineupUseCase
import com.pacho.regisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.regisoc.domain.usecase.GetPlayersUseCase
import com.pacho.regisoc.domain.usecase.SaveLineupUseCase
import com.pacho.regisoc.domain.validation.LineupValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LineupUiState {
    data object Loading : LineupUiState()
    data class Error(val message: String) : LineupUiState()
    data class Success(
        val lineupId: Long?,
        val isClosed: Boolean,
        val playerIds: Set<Long>,
        val coachId: Long?,
        val physicalTrainerId: Long?
    ) : LineupUiState()
}

class LineupViewModel(
    private val getLineupUseCase: GetLineupUseCase,
    private val saveLineupUseCase: SaveLineupUseCase,
    private val closeLineupUseCase: CloseLineupUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getCoachesUseCase: GetCoachesUseCase,
    private val getPhysicalTrainersUseCase: GetPhysicalTrainersUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LineupUiState>(LineupUiState.Loading)
    val uiState: StateFlow<LineupUiState> = _uiState.asStateFlow()

    private val _players = MutableStateFlow<List<PlayerResponse>>(emptyList())
    val players: StateFlow<List<PlayerResponse>> = _players.asStateFlow()

    private val _coaches = MutableStateFlow<List<CoachResponse>>(emptyList())
    val coaches: StateFlow<List<CoachResponse>> = _coaches.asStateFlow()

    private val _physicalTrainers = MutableStateFlow<List<PhysicalTrainerResponse>>(emptyList())
    val physicalTrainers: StateFlow<List<PhysicalTrainerResponse>> = _physicalTrainers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    private val matchId = MutableStateFlow(0L)

    val clubId: Long
        get() = sessionManager.clubId

    fun load(matchId: Long) {
        this.matchId.value = matchId
        val clubId = sessionManager.clubId
        viewModelScope.launch {
            _uiState.value = LineupUiState.Loading
            when (val result = getLineupUseCase(matchId, clubId)) {
                is Result.Error -> _uiState.value = LineupUiState.Error(result.message)
                is Result.Success -> {
                    val lineup = result.data
                    _uiState.value = LineupUiState.Success(
                        lineupId = lineup?.id,
                        isClosed = lineup?.isClosed ?: false,
                        playerIds = lineup?.playerIds ?: emptySet(),
                        coachId = lineup?.coach?.coachId,
                        physicalTrainerId = lineup?.physicalTrainer?.physicalTrainerId
                    )
                    loadAvailableMembers(clubId)
                }
            }
        }
    }

    private fun loadAvailableMembers(clubId: Long) {
        viewModelScope.launch {
            _players.value = getSuccessData(getPlayersUseCase(clubId))
            _coaches.value = getSuccessData(getCoachesUseCase(clubId))
            _physicalTrainers.value = getSuccessData(getPhysicalTrainersUseCase(clubId))
        }
    }

    private fun <T> getSuccessData(result: Result<List<T>>): List<T> {
        return (result as? Result.Success)?.data ?: emptyList()
    }

    fun togglePlayer(playerId: Long) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        val updated = if (playerId in state.playerIds) {
            state.playerIds - playerId
        } else {
            if (state.playerIds.size >= LineupValidator.MAX_PLAYERS) {
                viewModelScope.launch {
                    _snackBarMessage.emit("Máximo ${LineupValidator.MAX_PLAYERS} jugadores por planilla")
                }
                return
            }
            state.playerIds + playerId
        }
        _uiState.value = state.copy(playerIds = updated)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCoach(coachId: Long?) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return
        _uiState.value = state.copy(coachId = coachId)
    }

    fun selectPhysicalTrainer(trainerId: Long?) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return
        _uiState.value = state.copy(physicalTrainerId = trainerId)
    }

    fun saveDraft() {
        persist(isClose = false)
    }

    fun closeLineup() {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        if (state.lineupId != null) {
            viewModelScope.launch {
                _isSaving.value = true
                when (val result = closeLineupUseCase(state.lineupId)) {
                    is Result.Error -> {
                        _isSaving.value = false
                        _snackBarMessage.emit(result.message)
                    }
                    is Result.Success -> refreshFromServer("Planilla cerrada definitivamente")
                }
            }
        } else {
            persist(isClose = true)
        }
    }

    private fun persist(isClose: Boolean) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        viewModelScope.launch {
            _isSaving.value = true
            when (val result = saveLineupUseCase(
                id = state.lineupId,
                matchId = matchId.value,
                clubId = sessionManager.clubId,
                playerIds = state.playerIds.toList(),
                coachId = state.coachId,
                physicalTrainerId = state.physicalTrainerId
            )) {
                is Result.Error -> {
                    _isSaving.value = false
                    _snackBarMessage.emit(result.message)
                }
                is Result.Success -> {
                    val saved = result.data
                    if (!isClose) {
                        _isSaving.value = false
                        _uiState.value = state.copy(
                            lineupId = saved.id,
                            isClosed = saved.isClosed,
                            playerIds = saved.playerIds,
                            coachId = saved.coach?.coachId,
                            physicalTrainerId = saved.physicalTrainer?.physicalTrainerId
                        )
                        _snackBarMessage.emit("Planilla guardada como borrador")
                    } else {
                        // Cerrar una planilla recién creada exige llamar al endpoint
                        // de cierre: solo guardar la deja en OPEN en el backend.
                        when (val closeResult = closeLineupUseCase(saved.id)) {
                            is Result.Error -> {
                                _isSaving.value = false
                                _uiState.value = state.copy(
                                    lineupId = saved.id,
                                    isClosed = saved.isClosed,
                                    playerIds = saved.playerIds,
                                    coachId = saved.coach?.coachId,
                                    physicalTrainerId = saved.physicalTrainer?.physicalTrainerId
                                )
                                _snackBarMessage.emit(closeResult.message)
                            }
                            is Result.Success -> refreshFromServer("Planilla cerrada definitivamente")
                        }
                    }
                }
            }
        }
    }

    /**
     * Recarga la planilla desde el backend para que [LineupUiState.Success.isClosed]
     * refleje el estado real (CLOSE). Así la planilla queda bloqueada aunque se
     * salga de la pantalla y se vuelva a entrar.
     */
    private suspend fun refreshFromServer(successMessage: String) {
        when (val result = getLineupUseCase(matchId.value, sessionManager.clubId)) {
            is Result.Error -> {
                _isSaving.value = false
                _snackBarMessage.emit(result.message)
            }
            is Result.Success -> {
                _isSaving.value = false
                val lineup = result.data
                val state = currentSuccessState()
                if (state != null) {
                    _uiState.value = state.copy(
                        lineupId = lineup?.id ?: state.lineupId,
                        // Si el cierre tuvo éxito el registro existe; si por algún
                        // motivo ya no vuelve, no reabrir la edición por seguridad.
                        isClosed = lineup?.isClosed ?: true,
                        playerIds = lineup?.playerIds ?: state.playerIds,
                        coachId = lineup?.coach?.coachId ?: state.coachId,
                        physicalTrainerId = lineup?.physicalTrainer?.physicalTrainerId
                            ?: state.physicalTrainerId
                    )
                }
                _snackBarMessage.emit(successMessage)
            }
        }
    }

    private fun currentSuccessState(): LineupUiState.Success? {
        return _uiState.value as? LineupUiState.Success
    }
}
