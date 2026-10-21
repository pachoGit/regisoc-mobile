package com.pacho.regisoc.data.http

import io.ktor.client.plugins.api.*
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath

val AuthBearer = createClientPlugin("AuthBearer", ::AuthConfig) {
    val tokenProvider = pluginConfig.tokenProvider
    val excludedPaths = pluginConfig.excludedPaths

    onRequest { request, _ ->
        val path = request.url.encodedPath
        val isExcluded = excludedPaths.any { excluded -> path.contains(excluded) }

        if (!isExcluded) {
            val token = tokenProvider()
            if (!token.isNullOrBlank()) {
                request.headers.append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}
