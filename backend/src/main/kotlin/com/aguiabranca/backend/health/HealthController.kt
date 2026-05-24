package com.aguiabranca.backend.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/health")
open class HealthController {

    @GetMapping
    fun health(): Map<String, String> = mapOf("status" to "ok")
}

