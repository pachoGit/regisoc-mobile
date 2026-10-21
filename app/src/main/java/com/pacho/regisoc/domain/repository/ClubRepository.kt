package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.CreateClubRequest
import com.pacho.regisoc.data.dto.UpdateClubRequest
import kotlinx.coroutines.flow.Flow

interface ClubRepository {
    fun getClubs(): Flow<List<ClubResponse>>
    suspend fun createClub(request: CreateClubRequest): Result<ClubResponse>
    suspend fun updateClub(id: Long, request: UpdateClubRequest): Result<Unit>
    suspend fun deleteClub(id: Long): Result<Unit>
}
