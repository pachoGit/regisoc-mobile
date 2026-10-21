package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.domain.repository.ClubRepository
import kotlinx.coroutines.flow.Flow

class GetClubsUseCase(
    private val repository: ClubRepository
) {
    operator fun invoke(): Flow<List<ClubResponse>> = repository.getClubs()
}
