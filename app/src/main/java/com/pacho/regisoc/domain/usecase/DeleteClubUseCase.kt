package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.ClubRepository

class DeleteClubUseCase(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteClub(id)
}
