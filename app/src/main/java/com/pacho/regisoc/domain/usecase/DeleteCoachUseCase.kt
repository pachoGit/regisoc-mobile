package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.CoachRepository

class DeleteCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteCoach(id)
}
