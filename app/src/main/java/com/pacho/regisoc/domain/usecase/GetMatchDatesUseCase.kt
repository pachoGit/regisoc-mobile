package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.core.map
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.data.dto.MatchDateStatus
import com.pacho.regisoc.domain.repository.MatchDateRepository

class GetMatchDatesUseCase(
    private val repository: MatchDateRepository
) {
    suspend operator fun invoke(eventId: Long, clubId: Long): Result<List<MatchDateResponse>> {
        return repository.getByEventAndClub(eventId, clubId).map { matchDates ->
            matchDates.sortedWith(
                compareBy<MatchDateResponse> { it.status.order }
                    .thenBy { it.date }
                    .thenBy { it.id }
            )
        }
    }
}

private val MatchDateStatus.order: Int
    get() = when (this) {
        MatchDateStatus.UPCOMING -> 0
        MatchDateStatus.ONGOING -> 1
        MatchDateStatus.FINISHED -> 2
        MatchDateStatus.CANCELED -> 3
    }
