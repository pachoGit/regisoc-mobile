package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.PlayerRequest
import com.pacho.regisoc.data.dto.PlayerResponse
import com.pacho.regisoc.data.dto.PlayerUpdateRequest
import com.pacho.regisoc.domain.repository.PlayerRepository
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

class PlayerApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : PlayerRepository {
    override suspend fun getById(id: Long): Result<PlayerResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener jugador: ${response.status}")
            }
            Result.Success(response.body<PlayerResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener jugador: ${e.message}", e)
        }
    }

    override suspend fun getByClub(clubId: Long): Result<List<PlayerResponse>> {
        return try {
            val response = client.get {
                url(baseUrl)
                parameter("clubId", clubId)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener jugadores: ${response.status}")
            }
            val players = response.body<List<PlayerResponse>>()
            Result.Success(players)
        } catch (e: Exception) {
            Result.Error("Error al obtener jugadores: ${e.message}", e)
        }
    }

    override suspend fun createPlayer(player: PlayerResponse): Result<PlayerResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(
                    PlayerRequest(
                        clubId = player.clubId,
                        firstName = player.firstName,
                        lastName = player.lastName,
                        documentNumber = player.documentNumber,
                        age = player.age,
                        dateOfBirth = player.dateOfBirth,
                        position = player.position,
                        photoUrl = player.photoUrl,
                        documentFrontUrl = player.documentFrontUrl,
                        documentBackUrl = player.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear jugador: ${response.status}")
            }
            val created = response.body<PlayerResponse>()
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear jugador: ${e.message}", e)
        }
    }

    override suspend fun updatePlayer(id: Long, player: PlayerResponse): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(
                    PlayerUpdateRequest(
                        firstName = player.firstName,
                        lastName = player.lastName,
                        documentNumber = player.documentNumber,
                        age = player.age,
                        dateOfBirth = player.dateOfBirth,
                        position = player.position,
                        photoUrl = player.photoUrl,
                        documentFrontUrl = player.documentFrontUrl,
                        documentBackUrl = player.documentBackUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar jugador: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar jugador: ${e.message}", e)
        }
    }

    override suspend fun deletePlayer(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar jugador: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar jugador: ${e.message}", e)
        }
    }
}
