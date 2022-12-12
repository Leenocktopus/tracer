package com.leandoer.tracer.repository.projection

interface CountStatistics {
    val applications: Long
    val labels: Long
    val traces: Long
    val alerts: Long
    val alertEvents: Long
    val alertEventsNew: Long
    val alertEventsResolved: Long
}
