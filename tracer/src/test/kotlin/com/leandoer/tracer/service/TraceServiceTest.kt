package com.leandoer.tracer.service

import com.leandoer.tracer.repository.AlertEventRepository
import com.leandoer.tracer.repository.AlertRepository
import com.leandoer.tracer.repository.TestRunRepository
import com.leandoer.tracer.repository.TraceRepository
import com.leandoer.tracer.repository.specification.AlertEventSpecification
import com.leandoer.tracer.repository.specification.AlertSpecification
import com.leandoer.tracer.repository.specification.TraceSpecification
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TraceServiceTest(
    @MockK private val traceRepository: TraceRepository,
    @MockK private val applicationService: ApplicationService,
    @MockK private val labelService: LabelService,
    @MockK private val testRunRepository: TestRunRepository,
    @MockK private val alertRepository: AlertRepository,
    @MockK private val alertEventRepository: AlertEventRepository,
    @MockK private val alertEmailService: AlertEmailService
) {

    private val traceSpecification: TraceSpecification = spyk(TraceSpecification())
    private val alertSpecification: AlertSpecification = spyk(AlertSpecification())
    private val alertEventSpecification: AlertEventSpecification = spyk(AlertEventSpecification())

    private val traceService = TraceService(
        traceRepository,
        traceSpecification,
        applicationService,
        labelService,
        testRunRepository,
        alertRepository,
        alertSpecification,
        alertEventSpecification,
        alertEventRepository,
        alertEmailService
    )
}