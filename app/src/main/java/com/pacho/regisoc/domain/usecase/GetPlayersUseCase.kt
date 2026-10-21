package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayersUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<PlayerResponse>> = repository.getByClub(clubId)
}
