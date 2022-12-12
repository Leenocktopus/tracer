package com.leandoer.tracer.model.entity

import java.time.Duration

enum class Intervals(val interval: Duration, val scale: Int) {
    HOUR(Duration.ofHours(1), 1),
    DAY(Duration.ofDays(1), 30),
    WEEK(Duration.ofDays(7), 120),
    MONTH(Duration.ofDays(30), 720);
}