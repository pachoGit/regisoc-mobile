package com.pacho.regisoc.domain.validation

import com.pacho.regisoc.core.ValidationResult

object LineupValidator {

    const val MAX_PLAYERS = 22

    fun validate(playerIds: List<Long>): ValidationResult {
        val errors = mutableMapOf<String, String>()

        if (playerIds.size > MAX_PLAYERS) {
            errors["playerIds"] = "La planilla no puede tener más de $MAX_PLAYERS jugadores"
        }

        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
}