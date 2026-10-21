package com.pacho.regisoc.domain.validation

import com.pacho.regisoc.core.ValidationResult

object ClubValidator {

    fun validate(name: String): ValidationResult {
        val errors = mutableMapOf<String, String>()

        if (name.isBlank()) {
            errors["name"] = "El nombre es obligatorio"
        } else if (name.length < 2) {
            errors["name"] = "El nombre debe tener al menos 2 caracteres"
        }

        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
}
