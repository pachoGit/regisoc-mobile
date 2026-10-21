package com.pacho.regisoc.domain.repository

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.EventRequest
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.EventUpdateRequest

interface EventRepository {
    suspend fun getByClub(clubId: Long): Result<List<EventResponse>>
    suspend fun getById(id: Long): Result<EventResponse?>
    suspend fun createEvent(request: EventRequest): Result<EventResponse>
    suspend fun updateEvent(id: Long, request: EventUpdateRequest): Result<Unit>
    suspend fun deleteEvent(id: Long): Result<Unit>
}