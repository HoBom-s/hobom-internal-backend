package com.hobom.hobominternal.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyAuthFilter(
    @Value("\${hobom.admin.api-key}") private val apiKey: String,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val providedKey = request.getHeader("X-API-Key")
        if (providedKey != null && providedKey == apiKey) {
            val auth = UsernamePasswordAuthenticationToken(
                "api-key-user",
                null,
                listOf(SimpleGrantedAuthority("ROLE_ADMIN")),
            )
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }
}
