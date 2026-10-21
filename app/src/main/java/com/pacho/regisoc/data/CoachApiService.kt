package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.CoachRequest
import com.pacho.regisoc.data.dto.CoachResponse
import com.pacho.regisoc.data.dto.CoachUpdateRequest
import com.pacho.regisoc.domain.repository.CoachRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoachApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : CoachRepository {

    override suspend fun getById(id: Long): Result<CoachResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener entrenador: ${response.status}")
            }
            Result.Success(response.body<CoachResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener entrenador: ${e.message}", e)
        }
    }

    override suspend fun getByClub(clubId: Long): Result<List<CoachResponse>> {
        return try {
            val response = client.get {
                url(baseUrl)
                parameter("clubId", clubId)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener entrenadores: ${response.status}")
            }
            val coaches = response.body<List<CoachResponse>>()
            Result.Success(coaches)
        } catch (e: Exception) {
            Result.Error("Error al obtener entrenadores: ${e.message}", e)
        }
    }

    override suspend fun createCoach(coach: CoachResponse): Result<CoachResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(
                    CoachRequest(
                        clubId = coach.clubId,
                        firstName = coach.firstName,
                        lastName = coach.lastName,
                        documentNumber = coach.documentNumber,
                        age = coach.age,
                        dateOfBirth = coach.dateOfBirth,
                        photoUrl = coach.photoUrl,
                        documentFrontUrl = coach.documentFrontUrl,
                        documentBackUrl = coach.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear entrenador: ${response.status}")
            }
            val created = response.body<CoachResponse>()
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear entrenador: ${e.message}", e)
        }
    }

    override suspend fun updateCoach(id: Long, coach: CoachResponse): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(
                    CoachUpdateRequest(
                        firstName = coach.firstName,
                        lastName = coach.lastName,
                        documentNumber = coach.documentNumber,
                        age = coach.age,
                        dateOfBirth = coach.dateOfBirth,
                        photoUrl = coach.photoUrl,
                        documentFrontUrl = coach.documentFrontUrl,
                        documentBackUrl = coach.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar entrenador: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar entrenador: ${e.message}", e)
        }
    }

    override suspend fun deleteCoach(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar entrenador: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar entrenador: ${e.message}", e)
        }
    }
}
