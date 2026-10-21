package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.CoachResponse
import kotlinx.coroutines.flow.Flow

interface CoachRepository {
    suspend fun getById(id: Long): Result<CoachResponse?>
    suspend fun getByClub(clubId: Long): Result<List<CoachResponse>>
    suspend fun createCoach(coach: CoachResponse): Result<CoachResponse>
    suspend fun updateCoach(id: Long, coach: CoachResponse): Result<Unit>
    suspend fun deleteCoach(id: Long): Result<Unit>
}
