package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PhysicalTrainerRequest
import com.pacho.regisoc.data.dto.PhysicalTrainerResponse
import com.pacho.regisoc.data.dto.PhysicalTrainerUpdateRequest
import com.pacho.regisoc.domain.repository.PhysicalTrainerRepository
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

class PhysicalTrainerApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : PhysicalTrainerRepository {
    override suspend fun getById(id: Long): Result<PhysicalTrainerResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener preparador físico: ${response.status}")
            }
            Result.Success(response.body<PhysicalTrainerResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener preparador físico: ${e.message}", e)
        }
    }

    override suspend fun getByClub(clubId: Long): Result<List<PhysicalTrainerResponse>> {
        return try {
            val response = client.get {
                url(baseUrl)
                parameter("clubId", clubId)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener preparadores físicos: ${response.status}")
            }
            val trainers = response.body<List<PhysicalTrainerResponse>>()
            Result.Success(trainers)
        } catch (e: Exception) {
            Result.Error("Error al obtener preparadores físicos: ${e.message}", e)
        }
    }

    override suspend fun createPhysicalTrainer(trainer: PhysicalTrainerResponse): Result<PhysicalTrainerResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(
                    PhysicalTrainerRequest(
                        clubId = trainer.clubId,
                        firstName = trainer.firstName,
                        lastName = trainer.lastName,
                        documentNumber = trainer.documentNumber,
                        age = trainer.age,
                        dateOfBirth = trainer.dateOfBirth,
                        photoUrl = trainer.photoUrl,
                        documentFrontUrl = trainer.documentFrontUrl,
                        documentBackUrl = trainer.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear preparador físico: ${response.status}")
            }
            val created = response.body<PhysicalTrainerResponse>()
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear preparador físico: ${e.message}", e)
        }
    }

    override suspend fun updatePhysicalTrainer(id: Long, entity: PhysicalTrainerResponse): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(
                    PhysicalTrainerUpdateRequest(
                        firstName = entity.firstName,
                        lastName = entity.lastName,
                        documentNumber = entity.documentNumber,
                        age = entity.age,
                        dateOfBirth = entity.dateOfBirth,
                        photoUrl = entity.photoUrl,
                        documentFrontUrl = entity.documentFrontUrl,
                        documentBackUrl = entity.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar preparador físico: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar preparador físico: ${e.message}", e)
        }
    }

    override suspend fun deletePhysicalTrainer(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar preparador físico: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar preparador físico: ${e.message}", e)
        }
    }
}
