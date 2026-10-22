package com.pacho.regisoc.ui.navigation

import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchResponse
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.dto.PlayerResponse

sealed class Screen {
    data object Login : Screen()
    data object Home : Screen()
    data object About : Screen()

    sealed class Club : Screen() {
        data object List : Club()
        data object Create : Club()
        data class Edit(val club: ClubResponse) : Club()
        data class Detail(val club: ClubResponse) : Club()
    }

    sealed class Event : Screen() {
        data object List : Event()
        data object Create : Event()
        data class Edit(val event: EventResponse) : Event()
        data class Detail(val event: EventResponse) : Event()
        data class MatchDateList(val event: EventResponse) : Event()
        data class MatchDateDetail(val event: EventResponse, val matchDate: MatchDateResponse) : Event()
    }

    sealed class Staff : Screen() {
        data object Overview : Staff()
    }

    sealed class Player : Screen() {
        data object Create : Player()
        data class Edit(val player: PlayerResponse) : Player()
        data class Detail(val player: PlayerResponse) : Player()
    }

    sealed class Coach : Screen() {
        data object Create : Coach()
        data class Edit(val coach: CoachResponse) : Coach()
        data class Detail(val coach: CoachResponse) : Coach()
    }

    sealed class PhysicalTrainer : Screen() {
        data object Create : PhysicalTrainer()
        data class Edit(val trainer: PhysicalTrainerResponse) : PhysicalTrainer()
        data class Detail(val trainer: PhysicalTrainerResponse) : PhysicalTrainer()
    }

    data class Lineup(
        val match: MatchResponse,
        val matchDate: MatchDateResponse,
        val event: EventResponse
    ) : Screen()

    /**
     * Detalle de solo lectura de una planilla ya registrada, propia o rival.
     * [lineupClubId] es el club dueño de la planilla a consultar.
     */
    data class LineupDetail(
        val match: MatchResponse,
        val matchDate: MatchDateResponse,
        val event: EventResponse,
        val lineupClubId: Long,
        val clubName: String,
        val isMine: Boolean
    ) : Screen()
}