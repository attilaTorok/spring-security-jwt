package dev.attilatorok8.springsecurityjwt.controller

import dev.attilatorok8.springsecurityjwt.model.LoginRequest
import dev.attilatorok8.springsecurityjwt.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    @PostMapping("/token")
    fun token(@RequestBody userLogin: LoginRequest): String {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password))
        logger.info("Token requested for user: " + authentication.name)
        return tokenService.generateToken(authentication)
    }

}