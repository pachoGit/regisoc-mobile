package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import kotlinx.coroutines.flow.Flow

interface PhysicalTrainerRepository {
    suspend fun getById(id: Long): Result<PhysicalTrainerResponse?>
    suspend fun getByClub(clubId: Long): Result<List<PhysicalTrainerResponse>>
    suspend fun createPhysicalTrainer(trainer: PhysicalTrainerResponse): Result<PhysicalTrainerResponse>
    suspend fun updatePhysicalTrainer(id: Long, entity: PhysicalTrainerResponse): Result<Unit>
    suspend fun deletePhysicalTrainer(id: Long): Result<Unit>
}
