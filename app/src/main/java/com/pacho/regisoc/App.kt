package com.pacho.regisoc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pacho.regisoc.di.AppModule
import com.pacho.regisoc.ui.account.AccountActionsFactory
import com.pacho.regisoc.ui.account.LocalAccountActions
import com.pacho.regisoc.ui.components.LoadingOverlay
import com.pacho.regisoc.ui.features.about.AboutRoute
import com.pacho.regisoc.ui.features.club.ClubRoute
import com.pacho.regisoc.ui.features.club.ClubViewModel
import com.pacho.regisoc.ui.features.coach.CoachRoute
import com.pacho.regisoc.ui.features.coach.CoachViewModel
import com.pacho.regisoc.ui.features.event.EventRoute
import com.pacho.regisoc.ui.features.event.EventViewModel
import com.pacho.regisoc.ui.features.event.MatchDateViewModel
import com.pacho.regisoc.ui.features.home.HomeRoute
import com.pacho.regisoc.ui.features.lineup.LineupDetailRoute
import com.pacho.regisoc.ui.features.lineup.LineupDetailViewModel
import com.pacho.regisoc.ui.features.lineup.LineupRoute
import com.pacho.regisoc.ui.features.lineup.LineupViewModel
import com.pacho.regisoc.ui.features.login.LoginRoute
import com.pacho.regisoc.ui.features.login.LoginViewModel
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerRoute
import com.pacho.regisoc.ui.features.physicaltrainer.PhysicalTrainerViewModel
import com.pacho.regisoc.ui.features.player.PlayerRoute
import com.pacho.regisoc.ui.features.player.PlayerViewModel
import com.pacho.regisoc.ui.features.staff.StaffRoute
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen
import kotlinx.coroutines.flow.merge

@Composable
fun App() {
    val appModule = remember { AppModule() }
    val navigator = remember { AppNavigator() }
    val snackbarHostState = remember { SnackbarHostState() }
    val sessionManager = appModule.sessionManager

    val loginViewModel: LoginViewModel = viewModel {
        LoginViewModel(
            loginUseCase = appModule.loginUseCase,
            sessionManager = sessionManager
        )
    }
    val playerViewModel: PlayerViewModel = viewModel {
        PlayerViewModel(
            getPlayersUseCase = appModule.getPlayersUseCase,
            savePlayerUseCase = appModule.savePlayerUseCase,
            deletePlayerUseCase = appModule.deletePlayerUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase,
            sessionManager = sessionManager
        )
    }
    val clubViewModel: ClubViewModel = viewModel {
        ClubViewModel(
            getClubsUseCase = appModule.getClubsUseCase,
            createClubUseCase = appModule.createClubUseCase,
            updateClubUseCase = appModule.updateClubUseCase,
            deleteClubUseCase = appModule.deleteClubUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase
        )
    }
    val coachViewModel: CoachViewModel = viewModel {
        CoachViewModel(
            getCoachesUseCase = appModule.getCoachesUseCase,
            saveCoachUseCase = appModule.saveCoachUseCase,
            deleteCoachUseCase = appModule.deleteCoachUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase,
            sessionManager = sessionManager
        )
    }
    val physicalTrainerViewModel: PhysicalTrainerViewModel = viewModel {
        PhysicalTrainerViewModel(
            getPhysicalTrainersUseCase = appModule.getPhysicalTrainersUseCase,
            savePhysicalTrainerUseCase = appModule.savePhysicalTrainerUseCase,
            deletePhysicalTrainerUseCase = appModule.deletePhysicalTrainerUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase,
            sessionManager = sessionManager
        )
    }
    val eventViewModel: EventViewModel = viewModel {
        EventViewModel(
            getEventsUseCase = appModule.getEventsUseCase,
            createEventUseCase = appModule.createEventUseCase,
            updateEventUseCase = appModule.updateEventUseCase,
            deleteEventUseCase = appModule.deleteEventUseCase,
            sessionManager = sessionManager
        )
    }
    val matchDateViewModel: MatchDateViewModel = viewModel {
        MatchDateViewModel(
            getMatchDatesUseCase = appModule.getMatchDatesUseCase,
            sessionManager = sessionManager
        )
    }
    val lineupViewModel: LineupViewModel = viewModel {
        LineupViewModel(
            getLineupUseCase = appModule.getLineupUseCase,
            saveLineupUseCase = appModule.saveLineupUseCase,
            closeLineupUseCase = appModule.closeLineupUseCase,
            getPlayersUseCase = appModule.getPlayersUseCase,
            getCoachesUseCase = appModule.getCoachesUseCase,
            getPhysicalTrainersUseCase = appModule.getPhysicalTrainersUseCase,
            sessionManager = sessionManager
        )
    }
    val lineupDetailViewModel: LineupDetailViewModel = viewModel {
        LineupDetailViewModel(
            getLineupUseCase = appModule.getLineupUseCase
        )
    }

    val playerUiState by playerViewModel.uiState.collectAsState()
    val clubUiState by clubViewModel.uiState.collectAsState()
    val coachUiState by coachViewModel.uiState.collectAsState()
    val physicalTrainerUiState by physicalTrainerViewModel.uiState.collectAsState()
    val eventUiState by eventViewModel.uiState.collectAsState()
    val matchDateUiState by matchDateViewModel.uiState.collectAsState()
    val sessionData by sessionManager.sessionData.collectAsState()

    val playerIsLoading by playerViewModel.isLoading.collectAsState()
    val clubIsLoading by clubViewModel.isLoading.collectAsState()
    val coachIsLoading by coachViewModel.isLoading.collectAsState()
    val physicalTrainerIsLoading by physicalTrainerViewModel.isLoading.collectAsState()
    val eventIsLoading by eventViewModel.isLoading.collectAsState()

    var selectedStaffTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        merge(
            loginViewModel.snackBarMessage,
            playerViewModel.snackBarMessage,
            clubViewModel.snackBarMessage,
            coachViewModel.snackBarMessage,
            physicalTrainerViewModel.snackBarMessage,
            eventViewModel.snackBarMessage,
            lineupViewModel.snackBarMessage
        ).collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val accountActions = remember(sessionData, clubUiState) {
        AccountActionsFactory.create(
            sessionData = sessionData,
            clubsState = clubUiState,
            onAboutClick = { navigator.navigateTo(Screen.About) },
            onLogoutClick = {
                appModule.logoutUseCase()
                navigator.navigateToLogin()
            }
        )
    }

    CompositionLocalProvider(LocalAccountActions provides accountActions) {
        MaterialTheme {
            val isLoading = playerIsLoading || clubIsLoading || coachIsLoading || physicalTrainerIsLoading || eventIsLoading
            LoadingOverlay(isLoading = isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val snackbarHost: @Composable () -> Unit = { SnackbarHost(snackbarHostState) }
                    when (val screen = navigator.currentScreen) {
                        is Screen.Login -> LoginRoute(
                            viewModel = loginViewModel,
                            sessionManager = sessionManager,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.Home -> HomeRoute(
                            uiState = clubUiState,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.About -> AboutRoute(
                            sessionManager = sessionManager,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.Club -> ClubRoute(
                            screen = screen,
                            uiState = clubUiState,
                            viewModel = clubViewModel,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.Event -> EventRoute(
                            screen = screen,
                            uiState = eventUiState,
                            matchDateUiState = matchDateUiState,
                            viewModel = eventViewModel,
                            matchDateViewModel = matchDateViewModel,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.Staff -> StaffRoute(
                            uiState = playerUiState,
                            coachUiState = coachUiState,
                            physicalTrainerUiState = physicalTrainerUiState,
                            viewModel = playerViewModel,
                            coachViewModel = coachViewModel,
                            physicalTrainerViewModel = physicalTrainerViewModel,
                            navigator = navigator,
                            selectedTab = selectedStaffTab,
                            onSelectedTabChange = { selectedStaffTab = it },
                            snackbarHost = snackbarHost
                        )
                        is Screen.Player -> PlayerRoute(
                            screen = screen,
                            viewModel = playerViewModel,
                            navigator = navigator
                        )
                        is Screen.Coach -> CoachRoute(
                            screen = screen,
                            viewModel = coachViewModel,
                            navigator = navigator
                        )
                        is Screen.PhysicalTrainer -> PhysicalTrainerRoute(
                            screen = screen,
                            viewModel = physicalTrainerViewModel,
                            navigator = navigator
                        )
                        is Screen.Lineup -> LineupRoute(
                            screen = screen,
                            viewModel = lineupViewModel,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                        is Screen.LineupDetail -> LineupDetailRoute(
                            screen = screen,
                            viewModel = lineupDetailViewModel,
                            navigator = navigator,
                            snackbarHost = snackbarHost
                        )
                    }
                }
            }
        }
    }
}
