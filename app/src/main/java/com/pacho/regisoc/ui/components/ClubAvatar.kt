package com.pacho.regisoc.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pacho.regisoc.R

/**
 * Avatar circular con el escudo de un club.
 *
 * Muestra la foto remota ([crestUrl]) y, si no hay foto (o la carga falla),
 * una imagen por defecto. Reutilizado por el boton de cuenta y la pantalla "Sobre mi".
 */
@Composable
fun ClubAvatar(
    crestUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        val fallbackPainter = painterResource(R.drawable.compose_multiplatform)

        if (crestUrl.isNullOrBlank()) {
            Image(
                painter = fallbackPainter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = crestUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = fallbackPainter,
                fallback = fallbackPainter
            )
        }
    }
}
