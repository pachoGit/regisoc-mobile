package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.EventUpdateRequest
import com.pacho.regisoc.data.dto.EventStatus
import com.pacho.regisoc.domain.validation.EventValidator
import com.pacho.regisoc.domain.repository.EventRepository

class UpdateEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        description: String?,
        location: String?,
        startDate: String,
        endDate: String?,
        status: EventStatus
    ): Result<Unit> {
        val validation = EventValidator.validate(title = title, startDate = startDate)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        return repository.updateEvent(
            id = id,
            request = EventUpdateRequest(
                title = title.trim(),
                description = description?.ifBlank { null },
                location = location?.ifBlank { null },
                startDate = startDate.trim(),
                endDate = endDate?.ifBlank { null },
                status = status
            )
        )
    }
}