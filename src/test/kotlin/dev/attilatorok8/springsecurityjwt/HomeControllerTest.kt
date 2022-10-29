package dev.attilatorok8.springsecurityjwt

import dev.attilatorok8.springsecurityjwt.config.SecurityConfig
import dev.attilatorok8.springsecurityjwt.controller.AuthController
import dev.attilatorok8.springsecurityjwt.controller.HomeController
import dev.attilatorok8.springsecurityjwt.service.TokenService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(HomeController::class, AuthController::class)
@Import(SecurityConfig::class, TokenService::class)
internal class HomeControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun `root when unauthenticated then 401`() {
        mvc.perform(get("/"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @Throws(Exception::class)
    fun `root when authenticated then says hello user`() {
        val result: MvcResult = mvc.perform(post("/token")
            .with(httpBasic("attila", "password")))
            .andExpect(status().isOk)
            .andReturn()

        val token: String = result.response.contentAsString
        mvc.perform(
            get("/")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(content().string("Hello attila"))
    }

    @Test
    @WithMockUser
    @Throws(Exception::class)
    fun `root with mock user status is OK`() {
        mvc.perform(get("/")).andExpect(status().isOk)
    }
}