package com.pacho.regisoc.ui.features.event

import androidx.compose.runtime.Composable
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
                    onLoad = { matchDateViewModel.loadMatchDates(screen.event.id) },
                    onBack = { navigator.navigateTo(Screen.Event.Detail(screen.event)) },
                    onTabSelected = navigator::navigateToTab,
                    onViewDate = { navigator.navigateTo(Screen.Event.MatchDateDetail(screen.event, it)) },
                    onRegisterLineup = { matchDate ->
                        matchDate.match?.let { navigator.navigateTo(Screen.Lineup(it, matchDate, screen.event)) }
                    }
                )
            }
        }
        is Screen.Event.MatchDateDetail -> {
            MatchDateDetailScreen(
                matchDate = screen.matchDate,
                onBack = { navigator.navigateTo(Screen.Event.MatchDateList(screen.event)) },
                onTabSelected = navigator::navigateToTab,
                onOpenLineup = {
                    screen.matchDate.match?.let { navigator.navigateTo(Screen.Lineup(it, screen.matchDate, screen.event)) }
                }
            )
        }
    }
}