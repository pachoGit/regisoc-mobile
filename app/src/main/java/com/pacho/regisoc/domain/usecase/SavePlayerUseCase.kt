package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.core.map
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.domain.validation.PlayerValidator
import com.pacho.regisoc.domain.repository.PlayerRepository

class SavePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(
        id: Long?,
        firstName: String,
        lastName: String,
        documentNumber: String,
        age: String,
        dateOfBirth: String,
        clubId: Long,
        position: com.pacho.regisoc.data.dto.PlayerPosition? = null,
        photoUrl: String? = null,
        documentFrontUrl: String? = null,
        documentBackUrl: String? = null
    ): Result<Unit> {
        val validation = PlayerValidator.validate(firstName, lastName, documentNumber, age, dateOfBirth)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val player = PlayerResponse(
            id = id ?: 0L,
            clubId = clubId,
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            documentNumber = documentNumber.trim(),
            age = age.toInt(),
            dateOfBirth = dateOfBirth,
            position = position,
            photoUrl = photoUrl?.ifBlank { null },
            documentFrontUrl = documentFrontUrl?.ifBlank { null },
            documentBackUrl = documentBackUrl?.ifBlank { null }
        )

        return if (id != null) {
            repository.updatePlayer(id, player)
        } else {
            repository.createPlayer(player).map { Unit }
        }
    }
}
