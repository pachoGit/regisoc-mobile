package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.MatchDateResponse

interface MatchDateRepository {
    suspend fun getByEventAndClub(eventId: Long, clubId: Long): Result<List<MatchDateResponse>>
}
