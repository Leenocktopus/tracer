package com.leandoer.tracer.repository.projection

import com.leandoer.tracer.model.entity.Package
import com.leandoer.tracer.model.entity.TestType

interface TestStatistics {
    val testType: TestType
    val type: Package
    val count: Int
}
