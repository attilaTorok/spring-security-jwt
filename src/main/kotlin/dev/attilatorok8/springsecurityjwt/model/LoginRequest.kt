package dev.attilatorok8.springsecurityjwt.model

data class LoginRequest(
    val username: String,
    val password: String
)