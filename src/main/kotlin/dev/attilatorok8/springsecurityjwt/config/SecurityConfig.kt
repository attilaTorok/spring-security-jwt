package dev.attilatorok8.springsecurityjwt.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val rsaKeys: RsaKeyProperties
) {

    @Bean
    fun authManager(userDetailsService: UserDetailsService): AuthenticationManager =
        ProviderManager(DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
        })

    @Bean
    fun user(): InMemoryUserDetailsManager =
        InMemoryUserDetailsManager(
            User.withUsername("attila")
                .password("{noop}password")
                .authorities("read")
                .build()
        )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .authorizeRequests()
            .mvcMatchers("/token").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
            .exceptionHandling()
            .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
            .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            .and()
            .build()
    }

    @Bean
    protected fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }

    @Bean
    protected fun jwtEncoder(): JwtEncoder {
        val jwk = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        return NimbusJwtEncoder(ImmutableJWKSet<SecurityContext>(JWKSet(jwk)))
    }

}