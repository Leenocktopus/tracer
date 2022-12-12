package com.leandoer.tracer.model.dto.testrun

import javax.validation.constraints.Min

data class TestRunFilter(
    @Min(1)
    val traceId: Long?

)