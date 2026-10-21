package com.pacho.regisoc.di

import com.pacho.regisoc.data.AuthApiService
import com.pacho.regisoc.data.CoachApiService
import com.pacho.regisoc.data.ClubApiService
import com.pacho.regisoc.data.EventApiService
import com.pacho.regisoc.data.LineupApiService
import com.pacho.regisoc.data.MatchDateApiService
import com.pacho.regisoc.data.PhotoUploadDataSource
import com.pacho.regisoc.data.PhysicalTrainerApiService
import com.pacho.regisoc.data.PlayerApiService
import com.pacho.regisoc.data.apiBaseUrl
import com.pacho.regisoc.data.createHttpClient
import com.pacho.regisoc.data.http.AuthBearer
import com.pacho.regisoc.data.mock.MockPhotoUploadDataSource
import com.pacho.regisoc.data.session.SessionManager
import com.pacho.regisoc.domain.repository.AuthRepository
import com.pacho.regisoc.domain.repository.CoachRepository
import com.pacho.regisoc.domain.repository.ClubRepository
import com.pacho.regisoc.domain.repository.EventRepository
import com.pacho.regisoc.domain.repository.LineupRepository
import com.pacho.regisoc.domain.repository.MatchDateRepository
import com.pacho.regisoc.domain.repository.PhysicalTrainerRepository
import com.pacho.regisoc.domain.repository.PlayerRepository
import com.pacho.regisoc.domain.usecase.CloseLineupUseCase
import com.pacho.regisoc.domain.usecase.CreateClubUseCase
import com.pacho.regisoc.domain.usecase.CreateEventUseCase
import com.pacho.regisoc.domain.usecase.DeleteClubUseCase
import com.pacho.regisoc.domain.usecase.DeleteCoachUseCase
import com.pacho.regisoc.domain.usecase.DeleteEventUseCase
import com.pacho.regisoc.domain.usecase.DeletePhysicalTrainerUseCase
import com.pacho.regisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.regisoc.domain.usecase.GetClubsUseCase
import com.pacho.regisoc.domain.usecase.GetCoachesUseCase
import com.pacho.regisoc.domain.usecase.GetEventsUseCase
import com.pacho.regisoc.domain.usecase.GetMatchDatesUseCase
import com.pacho.regisoc.domain.usecase.GetLineupUseCase
import com.pacho.regisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.regisoc.domain.usecase.GetPlayersUseCase
import com.pacho.regisoc.domain.usecase.LoginUseCase
import com.pacho.regisoc.domain.usecase.LogoutUseCase
import com.pacho.regisoc.domain.usecase.SaveCoachUseCase
import com.pacho.regisoc.domain.usecase.SavePhysicalTrainerUseCase
import com.pacho.regisoc.domain.usecase.SaveLineupUseCase
import com.pacho.regisoc.domain.usecase.SavePlayerUseCase
import com.pacho.regisoc.domain.usecase.UpdateClubUseCase
import com.pacho.regisoc.domain.usecase.UpdateEventUseCase
import com.pacho.regisoc.domain.usecase.UploadPhotoUseCase

class AppModule {
    val sessionManager = SessionManager()

    private val httpClient = createHttpClient().config {
        install(AuthBearer) {
            tokenProvider = { sessionManager.token }
        }
    }

    private val playerRepository: PlayerRepository = PlayerApiService(httpClient, "$apiBaseUrl/players")

    private val clubRepository: ClubRepository = ClubApiService(httpClient, "$apiBaseUrl/clubs")

    private val coachRepository: CoachRepository = CoachApiService(httpClient, "$apiBaseUrl/coaches")

    private val physicalTrainerRepository: PhysicalTrainerRepository = PhysicalTrainerApiService(httpClient, "$apiBaseUrl/physical-trainers")

    private val eventRepository: EventRepository = EventApiService(httpClient, "$apiBaseUrl/events")

    private val matchDateRepository: MatchDateRepository = MatchDateApiService(httpClient, "$apiBaseUrl/match-dates")

    private val lineupRepository: LineupRepository = LineupApiService(httpClient, "$apiBaseUrl/lineups")

    private val authRepository: AuthRepository = AuthApiService(httpClient, "$apiBaseUrl/auth")

    private val photoUploadDataSource: PhotoUploadDataSource = MockPhotoUploadDataSource()

    val loginUseCase = LoginUseCase(authRepository, sessionManager)
    val logoutUseCase = LogoutUseCase(sessionManager)

    val getPlayersUseCase = GetPlayersUseCase(playerRepository)
    val savePlayerUseCase = SavePlayerUseCase(playerRepository)
    val deletePlayerUseCase = DeletePlayerUseCase(playerRepository)
    val uploadPhotoUseCase = UploadPhotoUseCase(photoUploadDataSource)

    val getCoachesUseCase = GetCoachesUseCase(coachRepository)
    val saveCoachUseCase = SaveCoachUseCase(coachRepository)
    val deleteCoachUseCase = DeleteCoachUseCase(coachRepository)

    val getPhysicalTrainersUseCase = GetPhysicalTrainersUseCase(physicalTrainerRepository)
    val savePhysicalTrainerUseCase = SavePhysicalTrainerUseCase(physicalTrainerRepository)
    val deletePhysicalTrainerUseCase = DeletePhysicalTrainerUseCase(physicalTrainerRepository)

    val getClubsUseCase = GetClubsUseCase(clubRepository)
    val createClubUseCase = CreateClubUseCase(clubRepository)
    val updateClubUseCase = UpdateClubUseCase(clubRepository)
    val deleteClubUseCase = DeleteClubUseCase(clubRepository)

    val getEventsUseCase = GetEventsUseCase(eventRepository)
    val createEventUseCase = CreateEventUseCase(eventRepository)
    val updateEventUseCase = UpdateEventUseCase(eventRepository)
    val deleteEventUseCase = DeleteEventUseCase(eventRepository)

    val getMatchDatesUseCase = GetMatchDatesUseCase(matchDateRepository)

    val getLineupUseCase = GetLineupUseCase(lineupRepository)
    val saveLineupUseCase = SaveLineupUseCase(lineupRepository)
    val closeLineupUseCase = CloseLineupUseCase(lineupRepository)
}
