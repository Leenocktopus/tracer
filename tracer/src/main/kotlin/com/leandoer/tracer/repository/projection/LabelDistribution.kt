package com.leandoer.tracer.repository.projection

interface LabelDistribution {
    val start: Long
    val end: Long
    val count: Long
}
