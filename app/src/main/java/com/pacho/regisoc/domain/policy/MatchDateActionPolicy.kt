package com.pacho.regisoc.domain.policy

import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus

/**
 * Acciones que el administrador puede realizar sobre una fecha del evento.
 *
 * Reglas:
 *  - Sin rival asignado: solo ver el detalle de la fecha.
 *  - Con rival y fecha PROGRAMADA: registrar plantilla y ver detalle.
 *  - Con rival y fecha EN CURSO / FINALIZADA / CANCELADA: ver mi plantilla,
 *    ver la plantilla rival y ver detalle.
 */
data class MatchDateActions(
    val canViewDate: Boolean = true,
    val canRegisterLineup: Boolean = false,
    val canViewMyLineup: Boolean = false,
    val canViewRivalLineup: Boolean = false
) {
    companion object {
        val ViewDateOnly = MatchDateActions()
    }
}

object MatchDateActionPolicy {

    fun actionsFor(matchDate: MatchDateResponse, myClubId: Long): MatchDateActions {
        // Sin rival asignado solo queda ver el detalle de la fecha.
        if (matchDate.rivalClub(myClubId) == null) return MatchDateActions.ViewDateOnly

        return when (matchDate.status) {
            MatchDateStatus.UPCOMING -> MatchDateActions(
                canViewDate = true,
                canRegisterLineup = true
            )
            MatchDateStatus.ONGOING,
            MatchDateStatus.FINISHED,
            MatchDateStatus.CANCELED -> MatchDateActions(
                canViewDate = true,
                canViewMyLineup = true,
                canViewRivalLineup = true
            )
        }
    }
}

/**
 * Club del usuario logueado dentro del partido de la fecha.
 * Devuelve null si la fecha no tiene partido o mi club no participa.
 */
fun MatchDateResponse.myClub(clubId: Long): ClubResponse? {
    val match = match ?: return null
    return when (clubId) {
        match.homeClub?.id -> match.homeClub
        match.awayClub?.id -> match.awayClub
        else -> null
    }
}

/** Club contrario a [clubId] en el partido de la fecha, o null si no hay rival. */
fun MatchDateResponse.rivalClub(clubId: Long): ClubResponse? {
    val match = match ?: return null
    return when (clubId) {
        match.homeClub?.id -> match.awayClub
        match.awayClub?.id -> match.homeClub
        else -> null
    }
}
