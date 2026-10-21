package com.pacho.regisoc.data.http

data class AuthConfig(
    var tokenProvider: () -> String? = { null },
    var excludedPaths: Set<String> = defaultExcludedPaths
) {
    companion object {
        val defaultExcludedPaths = setOf(
            "/auth/login"
        )
    }
}
