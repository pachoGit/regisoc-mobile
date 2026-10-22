package com.pacho.regisoc.ui.features.event

import androidx.compose.runtime.Composable
import com.pacho.regisoc.data.dto.EventResponse
import com.pacho.regisoc.data.dto.MatchDateResponse
import com.pacho.regisoc.domain.policy.myClub
import com.pacho.regisoc.domain.policy.rivalClub
import com.pacho.regisoc.ui.layouts.MainLayout
import com.pacho.regisoc.ui.navigation.AppNavigator
import com.pacho.regisoc.ui.navigation.Screen

@Composable
fun EventRoute(
    screen: Screen.Event,
    uiState: EventUiState,
    matchDateUiState: MatchDateUiState,
    viewModel: EventViewModel,
    matchDateViewModel: MatchDateViewModel,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    when (screen) {
        is Screen.Event.List -> {
            MainLayout(
                title = "Eventos",
                selectedTab = 1,
                onTabSelected = navigator::navigateToTab,
                snackbarHost = snackbarHost
            ) {
                EventListScreen(
                    uiState = uiState,
                    onLoad = viewModel::loadEvents,
                    onAddEvent = { navigator.navigateTo(Screen.Event.Create) },
                    onViewEvent = { navigator.navigateTo(Screen.Event.Detail(it)) },
                    onViewDates = { navigator.navigateTo(Screen.Event.MatchDateList(it)) }
                )
            }
        }
        is Screen.Event.Create -> {
            EventCreateScreen(
                onSave = { formState ->
                    viewModel.createEvent(formState)
                    navigator.navigateTo(Screen.Event.List)
                },
                onBack = { navigator.navigateTo(Screen.Event.List) },
                onTabSelected = navigator::navigateToTab
            )
        }
        is Screen.Event.Edit -> {
            EventEditScreen(
                event = screen.event,
                onSave = { formState ->
                    viewModel.updateEvent(formState)
                    navigator.navigateTo(Screen.Event.List)
                },
                onBack = { navigator.navigateTo(Screen.Event.List) },
                onTabSelected = navigator::navigateToTab
            )
        }
        is Screen.Event.Detail -> {
            EventDetailScreen(
                event = screen.event,
                onBack = { navigator.navigateTo(Screen.Event.List) },
                onViewDates = { navigator.navigateTo(Screen.Event.MatchDateList(screen.event)) },
                onTabSelected = navigator::navigateToTab
            )
        }
        is Screen.Event.MatchDateList -> {
            MainLayout(
                title = "Fechas del Evento",
                selectedTab = 1,
                onTabSelected = navigator::navigateToTab,
                snackbarHost = snackbarHost
            ) {
                MatchDateListScreen(
                    event = screen.event,
                    uiState = matchDateUiState,
                    myClubId = matchDateViewModel.clubId,
                    onLoad = { matchDateViewModel.loadMatchDates(screen.event.id) },
                    onViewDate = { navigator.navigateTo(Screen.Event.MatchDateDetail(screen.event, it)) },
                    onRegisterLineup = { matchDate ->
                        matchDate.match?.let { navigator.navigateTo(Screen.Lineup(it, matchDate, screen.event)) }
                    },
                    onViewMyLineup = { matchDate ->
                        navigateToLineupDetail(
                            navigator = navigator,
                            event = screen.event,
                            matchDate = matchDate,
                            myClubId = matchDateViewModel.clubId,
                            isMine = true
                        )
                    },
                    onViewRivalLineup = { matchDate ->
                        navigateToLineupDetail(
                            navigator = navigator,
                            event = screen.event,
                            matchDate = matchDate,
                            myClubId = matchDateViewModel.clubId,
                            isMine = false
                        )
                    }
                )
            }
        }
        is Screen.Event.MatchDateDetail -> {
            MatchDateDetailScreen(
                matchDate = screen.matchDate,
                myClubId = matchDateViewModel.clubId,
                onBack = { navigator.navigateTo(Screen.Event.MatchDateList(screen.event)) },
                onTabSelected = navigator::navigateToTab,
                onRegisterLineup = {
                    screen.matchDate.match?.let { navigator.navigateTo(Screen.Lineup(it, screen.matchDate, screen.event)) }
                },
                onViewMyLineup = {
                    navigateToLineupDetail(
                        navigator = navigator,
                        event = screen.event,
                        matchDate = screen.matchDate,
                        myClubId = matchDateViewModel.clubId,
                        isMine = true
                    )
                },
                onViewRivalLineup = {
                    navigateToLineupDetail(
                        navigator = navigator,
                        event = screen.event,
                        matchDate = screen.matchDate,
                        myClubId = matchDateViewModel.clubId,
                        isMine = false
                    )
                }
            )
        }
    }
}

/**
 * Navega al detalle de la plantilla propia o rival de una fecha.
 * Resuelve el club dueño de la planilla a partir de [isMine] y del club del usuario.
 */
private fun navigateToLineupDetail(
    navigator: AppNavigator,
    event: EventResponse,
    matchDate: MatchDateResponse,
    myClubId: Long,
    isMine: Boolean
) {
    val match = matchDate.match ?: return
    val lineupClub = if (isMine) {
        matchDate.myClub(myClubId)
    } else {
        matchDate.rivalClub(myClubId)
    } ?: return

    navigator.navigateTo(
        Screen.LineupDetail(
            match = match,
            matchDate = matchDate,
            event = event,
            lineupClubId = lineupClub.id,
            clubName = lineupClub.name,
            isMine = isMine
        )
    )
}