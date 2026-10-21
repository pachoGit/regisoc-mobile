package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.LineupResponse
import com.pacho.regisoc.data.dto.SetLineupRequest
import com.pacho.regisoc.domain.repository.LineupRepository
import com.pacho.regisoc.domain.validation.LineupValidator

class SaveLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(
        id: Long?,
        matchId: Long,
        clubId: Long,
        playerIds: List<Long>,
        coachId: Long?,
        physicalTrainerId: Long?
    ): Result<LineupResponse> {
        val validation = LineupValidator.validate(playerIds)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val request = SetLineupRequest(
            matchId = matchId,
            clubId = clubId,
            playerIds = playerIds.distinct(),
            coachId = coachId,
            physicalTrainerId = physicalTrainerId
        )

        return if (id == null) {
            repository.create(request)
        } else {
            repository.update(id, request)
        }
    }
}
