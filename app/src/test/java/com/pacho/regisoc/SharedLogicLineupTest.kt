package com.pacho.regisoc

import com.pacho.regisoc.domain.validation.LineupValidator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SharedLogicLineupTest {

    @Test
    fun validateAcceptsUpToMaxPlayers() {
        val result = LineupValidator.validate(List(LineupValidator.MAX_PLAYERS) { it.toLong() })
        assertTrue(result.isValid)
    }

    @Test
    fun validateRejectsMoreThanMaxPlayers() {
        val result = LineupValidator.validate(List(LineupValidator.MAX_PLAYERS + 1) { it.toLong() })
        assertFalse(result.isValid)
        assertTrue(result.errors.containsKey("playerIds"))
    }

    @Test
    fun validateAcceptsEmptyLineup() {
        val result = LineupValidator.validate(emptyList())
        assertTrue(result.isValid)
    }
}