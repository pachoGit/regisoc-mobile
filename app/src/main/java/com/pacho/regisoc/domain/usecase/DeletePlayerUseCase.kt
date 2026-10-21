package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.PlayerRepository

class DeletePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deletePlayer(id)
}
