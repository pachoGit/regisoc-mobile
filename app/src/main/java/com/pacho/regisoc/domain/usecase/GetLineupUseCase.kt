package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.LineupResponse
import com.pacho.regisoc.domain.repository.LineupRepository

class GetLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(matchId: Long, clubId: Long): Result<LineupResponse?> {
        return repository.getByMatch(matchId, clubId)
    }
}
