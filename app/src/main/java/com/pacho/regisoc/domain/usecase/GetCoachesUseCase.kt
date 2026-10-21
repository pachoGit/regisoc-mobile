package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.domain.repository.CoachRepository
import kotlinx.coroutines.flow.Flow

class GetCoachesUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<CoachResponse>> = repository.getByClub(clubId)
}
