package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.domain.repository.PhysicalTrainerRepository
import kotlinx.coroutines.flow.Flow

class GetPhysicalTrainersUseCase(
    private val repository: PhysicalTrainerRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<PhysicalTrainerResponse>> = repository.getByClub(clubId)
}
