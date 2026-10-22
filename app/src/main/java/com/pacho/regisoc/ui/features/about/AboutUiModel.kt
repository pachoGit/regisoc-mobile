package com.pacho.regisoc.ui.features.about

import com.pacho.regisoc.data.session.SessionData

/**
 * Modelo de vista de la pantalla "Sobre mi": datos de la sesion ya formateados,
 * de modo que el composable no conozca el dominio (facilita previews y testeo).
 */
data class AboutUiModel(
    val fullName: String,
    val username: String,
    val roleLabel: String,
    val documentNumber: String,
    val clubName: String,
    val clubCrestUrl: String?
) {
    companion object {
        private const val PLACEHOLDER = "No registrado"

        val Empty = AboutUiModel(
            fullName = PLACEHOLDER,
            username = PLACEHOLDER,
            roleLabel = PLACEHOLDER,
            documentNumber = PLACEHOLDER,
            clubName = "Sin club asignado",
            clubCrestUrl = null
        )
    }
}

fun SessionData.toAboutUiModel(clubCrestUrl: String?): AboutUiModel {
    val me = me
    return AboutUiModel(
        fullName = me
            ?.let { "${it.name} ${it.surname}".trim() }
            ?.ifBlank { null }
            ?: username.ifBlank { null }
            ?: AboutUiModel.Empty.fullName,
        username = me?.username?.ifBlank { null }
            ?: username.ifBlank { null }
            ?: AboutUiModel.Empty.username,
        roleLabel = roleLabel(me?.role?.ifBlank { null } ?: role),
        documentNumber = me?.documentNumber?.ifBlank { null }
            ?: AboutUiModel.Empty.documentNumber,
        clubName = me?.club?.name?.ifBlank { null }
            ?: AboutUiModel.Empty.clubName,
        clubCrestUrl = clubCrestUrl
    )
}

private fun roleLabel(role: String?): String = when (role) {
    "CLUB_MANAGER" -> "Administrador de club"
    null, "" -> "No registrado"
    else -> role
}
