package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.core.ValidationResult
import com.pacho.regisoc.data.dto.UpdateClubRequest
import com.pacho.regisoc.domain.repository.ClubRepository

class UpdateClubUseCase(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(
        id: Long,
        name: String,
        foundedYear: Int?,
        crestUrl: String?,
        description: String?
    ): Result<Unit> {
        val errors = mutableMapOf<String, String>()
        if (name.isBlank()) {
            errors["name"] = "El nombre es obligatorio"
        } else if (name.length < 2) {
            errors["name"] = "El nombre debe tener al menos 2 caracteres"
        }

        val validation = ValidationResult(errors.isEmpty(), errors)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val request = UpdateClubRequest(
            name = name.trim(),
            foundedYear = foundedYear,
            crestUrl = crestUrl?.ifBlank { null },
            description = description?.ifBlank { null }
        )

        return repository.updateClub(id, request)
    }
}
