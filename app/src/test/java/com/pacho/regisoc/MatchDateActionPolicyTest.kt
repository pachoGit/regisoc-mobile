package com.pacho.regisoc

import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.data.dto.MatchResponse
import com.pacho.regisoc.domain.policy.MatchDateActionPolicy
import com.pacho.regisoc.domain.policy.myClub
import com.pacho.regisoc.domain.policy.rivalClub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MatchDateActionPolicyTest {

    private val myClub = ClubResponse(id = 1, name = "Mi Club")
    private val rivalClub = ClubResponse(id = 2, name = "Rival")
    private val myClubId = myClub.id

    private fun matchDate(
        status: MatchDateStatus,
        home: ClubResponse? = myClub,
        away: ClubResponse? = rivalClub
    ) = MatchDateResponse(
        id = 1,
        date = "2026-08-05",
        status = status,
        name = "Fecha 1",
        match = home?.let {
            MatchResponse(id = 10, homeClub = it, awayClub = away)
        }
    )

    @Test
    fun `sin rival asignado solo permite ver el detalle`() {
        val actions = MatchDateActionPolicy.actionsFor(
            matchDate = matchDate(MatchDateStatus.UPCOMING, away = null),
            myClubId = myClubId
        )

        assertTrue(actions.canViewDate)
        assertFalse(actions.canRegisterLineup)
        assertFalse(actions.canViewMyLineup)
        assertFalse(actions.canViewRivalLineup)
    }

    @Test
    fun `fecha programada con rival permite registrar plantilla`() {
        val actions = MatchDateActionPolicy.actionsFor(
            matchDate = matchDate(MatchDateStatus.UPCOMING),
            myClubId = myClubId
        )

        assertTrue(actions.canViewDate)
        assertTrue(actions.canRegisterLineup)
        assertFalse(actions.canViewMyLineup)
        assertFalse(actions.canViewRivalLineup)
    }

    @Test
    fun `fechas en curso finalizada y cancelada permiten ver ambas plantillas`() {
        val statuses = listOf(
            MatchDateStatus.ONGOING,
            MatchDateStatus.FINISHED,
            MatchDateStatus.CANCELED
        )

        statuses.forEach { status ->
            val actions = MatchDateActionPolicy.actionsFor(
                matchDate = matchDate(status),
                myClubId = myClubId
            )

            assertTrue(actions.canViewDate, "canViewDate debe ser true para $status")
            assertFalse(actions.canRegisterLineup, "no se puede registrar en $status")
            assertTrue(actions.canViewMyLineup, "canViewMyLineup debe ser true para $status")
            assertTrue(actions.canViewRivalLineup, "canViewRivalLineup debe ser true para $status")
        }
    }

    @Test
    fun `mi club y rival se resuelven tanto como local como visitante`() {
        val homeMatch = matchDate(MatchDateStatus.UPCOMING, home = myClub, away = rivalClub)
        val awayMatch = matchDate(MatchDateStatus.UPCOMING, home = rivalClub, away = myClub)

        assertEquals(myClub.id, homeMatch.myClub(myClubId)?.id)
        assertEquals(rivalClub.id, homeMatch.rivalClub(myClubId)?.id)

        assertEquals(myClub.id, awayMatch.myClub(myClubId)?.id)
        assertEquals(rivalClub.id, awayMatch.rivalClub(myClubId)?.id)
    }

    @Test
    fun `club que no participa del partido no tiene rival`() {
        val matchDate = matchDate(MatchDateStatus.UPCOMING)

        assertNull(matchDate.rivalClub(clubId = 99))
        assertNull(matchDate.myClub(clubId = 99))
    }
}
