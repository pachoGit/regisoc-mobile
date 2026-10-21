package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.PhysicalTrainerRepository

class DeletePhysicalTrainerUseCase(
    private val repository: PhysicalTrainerRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deletePhysicalTrainer(id)
}
