package com.pacho.regisoc.domain.validation

import com.pacho.regisoc.core.ValidationResult

object PhysicalTrainerValidator {

    fun validate(
        firstName: String,
        lastName: String,
        documentNumber: String,
        age: String,
        dateOfBirth: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()

        if (firstName.isBlank()) {
            errors["firstName"] = "El nombre es obligatorio"
        } else if (firstName.length < 2) {
            errors["firstName"] = "El nombre debe tener al menos 2 caracteres"
        }

        if (lastName.isBlank()) {
            errors["lastName"] = "Los apellidos son obligatorios"
        } else if (lastName.length < 2) {
            errors["lastName"] = "Los apellidos deben tener al menos 2 caracteres"
        }

        if (documentNumber.isBlank()) {
            errors["documentNumber"] = "El DNI es obligatorio"
        } else if (!documentNumber.matches(Regex("^\\d{8}$"))) {
            errors["documentNumber"] = "El DNI debe tener 8 dígitos"
        }

        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 0 || ageInt > 120) {
            errors["age"] = "Ingrese una edad válida (0-120)"
        }

        if (dateOfBirth.isBlank()) {
            errors["dateOfBirth"] = "Seleccione una fecha de nacimiento"
        }

        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
}
