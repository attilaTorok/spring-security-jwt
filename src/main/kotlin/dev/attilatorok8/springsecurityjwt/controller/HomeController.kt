package dev.attilatorok8.springsecurityjwt.controller

import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class HomeController {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    @GetMapping
    fun home(principal: Principal): String {
        logger.info(principal.name + " called /")
        return "Hello " + principal.name
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/secure")
    fun secure(): String {
        logger.info("secure called")
        return "This is secured!"
    }

}