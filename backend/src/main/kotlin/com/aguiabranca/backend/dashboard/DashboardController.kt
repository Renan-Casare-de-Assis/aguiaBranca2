package com.aguiabranca.backend.dashboard

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
open class DashboardController(private val repository: DashboardRepository) {

    @GetMapping("/metrics")
    fun metrics(): DashboardMetricsResponse = repository.metrics()
}

