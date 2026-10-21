package com.pacho.regisoc.domain.usecase

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.domain.repository.EventRepository

class DeleteEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteEvent(id)
}