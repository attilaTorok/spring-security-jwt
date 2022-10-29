package dev.attilatorok8.springsecurityjwt.controller

import dev.attilatorok8.springsecurityjwt.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val tokenService: TokenService
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    @PostMapping("/token")
    fun token(authentication: Authentication): String? {
        logger.debug("Token requested for user: " + authentication.name)
        return tokenService.generateToken(authentication)
    }

}