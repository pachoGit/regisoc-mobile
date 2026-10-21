package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.LineupRepository

class CloseLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.close(id)
    }
}