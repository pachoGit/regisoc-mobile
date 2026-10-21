package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PlayerResponse
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getById(id: Long): Result<PlayerResponse?>
    suspend fun getByClub(clubId: Long): Result<List<PlayerResponse>>
    suspend fun createPlayer(player: PlayerResponse): Result<PlayerResponse>
    suspend fun updatePlayer(id: Long, player: PlayerResponse): Result<Unit>
    suspend fun deletePlayer(id: Long): Result<Unit>
}
