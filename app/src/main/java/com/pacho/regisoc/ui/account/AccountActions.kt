package com.pacho.regisoc.ui.account

import androidx.compose.runtime.staticCompositionLocalOf
import com.pacho.regisoc.data.session.SessionData
import com.pacho.regisoc.ui.features.club.ClubUiState

/**
 * Estado y callbacks del menu de cuenta de la barra superior.
 *
 * Se expone mediante [LocalAccountActions] para que el [com.pacho.regisoc.ui.layouts.MainLayout]
 * pueda pintar el menu sin arrastrar dependencias de sesion/clubes por todas las rutas
 * (inyeccion de dependencias a nivel de composicion).
 */
data class AccountActions(
    val clubCrestUrl: String? = null,
    val userName: String = "",
    val onAboutClick: () -> Unit = {},
    val onLogoutClick: () -> Unit = {}
) {
    companion object {
        val None = AccountActions()
    }
}

val LocalAccountActions = staticCompositionLocalOf { AccountActions.None }

/**
 * Construye [AccountActions] a partir de la sesion activa y los clubes cargados.
 *
 * La imagen del boton de cuenta es el escudo del club que administra el usuario;
 * si el club no tiene foto se deja en null para que la UI use la imagen por defecto.
 */
object AccountActionsFactory {

    fun create(
        sessionData: SessionData,
        clubsState: ClubUiState,
        onAboutClick: () -> Unit,
        onLogoutClick: () -> Unit
    ): AccountActions = AccountActions(
        clubCrestUrl = clubsState.crestUrlOf(sessionData.clubId),
        userName = sessionData.fullName,
        onAboutClick = onAboutClick,
        onLogoutClick = onLogoutClick
    )

    private fun ClubUiState.crestUrlOf(clubId: Long): String? =
        (this as? ClubUiState.Success)
            ?.clubs
            ?.firstOrNull { it.id == clubId }
            ?.crestUrl

    private val SessionData.fullName: String
        get() = me
            ?.let { "${it.name} ${it.surname}".trim() }
            ?.ifBlank { null }
            ?: username
}
