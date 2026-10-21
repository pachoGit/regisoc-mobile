package com.pacho.regisoc.data

import com.pacho.regisoc.core.Result
import com.pacho.regisoc.data.dto.ClubResponse
import com.pacho.regisoc.data.dto.CreateClubRequest
import com.pacho.regisoc.data.dto.UpdateClubRequest
import com.pacho.regisoc.domain.repository.ClubRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClubApiService(
    private val client: HttpClient,
    private val baseUrl: String
) : ClubRepository {

    private val clubsFlow = MutableStateFlow<List<ClubResponse>>(emptyList())

    override fun getClubs(): Flow<List<ClubResponse>> = clubsFlow.asStateFlow()

    override suspend fun createClub(request: CreateClubRequest): Result<ClubResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear club: ${response.status}")
            }
            val created = response.body<ClubResponse>()
            clubsFlow.value = clubsFlow.value + created
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear club: ${e.message}", e)
        }
    }

    override suspend fun updateClub(id: Long, request: UpdateClubRequest): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar club: ${response.status}")
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar club: ${e.message}", e)
        }
    }

    override suspend fun deleteClub(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar club: ${response.status}")
            }
            clubsFlow.value = clubsFlow.value.filter { it.id != id }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar club: ${e.message}", e)
        }
    }
}
