package com.leandoer.tracer.service

import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.trace.GetTraceDto
import com.leandoer.tracer.model.dto.trace.PostTraceDto
import com.leandoer.tracer.model.dto.trace.TraceFilter
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.AlertStatus
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.repository.AlertEventRepository
import com.leandoer.tracer.repository.AlertRepository
import com.leandoer.tracer.repository.TestRunRepository
import com.leandoer.tracer.repository.TraceRepository
import com.leandoer.tracer.repository.specification.AlertEventSpecification
import com.leandoer.tracer.repository.specification.AlertSpecification
import com.leandoer.tracer.repository.specification.TraceSpecification
import com.leandoer.tracer.service.alerts.AlertProcessor
import com.leandoer.tracer.service.alerts.TestAlertProcessor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ThreadLocalRandom


@Service
class TraceService(
    private val traceRepository: TraceRepository,
    private val traceSpecification: TraceSpecification,
    private val applicationService: ApplicationService,
    private val labelService: LabelService,
    private val testRunRepository: TestRunRepository,
    private val alertRepository: AlertRepository,
    private val alertSpecification: AlertSpecification,
    private val alertEventSpecification: AlertEventSpecification,
    private val alertEventRepository: AlertEventRepository,
    private val alertEmailService: AlertEmailService
) {

    fun findAll(traceFilter: TraceFilter, pageable: Pageable): Page<GetTraceDto> {
        val specification = with(traceSpecification) {
            applyTraceFilter(traceFilter)
                .and(fetchLabels())
                .and(fetchApplications())
                .and(fetchTestRuns())
        }
        return traceRepository.findAll(specification, pageable).map { map(it) };
    }

    @Transactional
    fun create(trace: PostTraceDto) {
        var newTrace = map(
            trace,
            { application -> applicationService.findByName(application) ?: applicationService.create(application) },
            { label -> labelService.findByLabel(label) }
        )

        newTrace = traceRepository.save(newTrace)
        val testRuns = testRunRepository.saveAll(
            TestChain.chain.executeNext(
                newTrace.labels.flatMap { it.tests }.distinct(),
                newTrace
            )
        )
        triggerAlerts(newTrace, testRuns)
    }


    fun triggerAlerts(trace: Trace, testRuns: List<TestRun>) {
        if (ThreadLocalRandom.current().nextBoolean() && ThreadLocalRandom.current().nextBoolean()) {
            val specification = with(alertSpecification) {
                mappedToLabels(trace.labels.map { it.id!! })
                    .and(fetchContacts())
                    .and(fetchLabels())
            }
            val alerts = alertRepository.findAll(specification)
            val alertEvents = mutableSetOf<AlertEvent>()
            if (alerts.isNotEmpty()) {
                val processors = AlertProcessor.link(TestAlertProcessor())
                alertEvents.addAll(processors.executeNext(trace, testRuns, alerts))
            }

//        val alertSet = alerts.flatMap { it.labels }.mapNotNull { it.id }.distinct().toSet()
//
//        val alertEventsLastHour = alertEventRepository.findAll(alertEventSpecification.lastHour())

//        trace.labels.filter { alertSet.contains(it.id!!) }
//            .forEach { label ->
//                val distribution = traceRepository.getLabelDistribution(label.id!!)
//                val average = distribution.map { it.count }.average()
//                val sum = distribution.map { it.count }.sum()
//                val activated = distribution.filter { Math.abs(it.count - average) / average > 1.2 }
//                if (activated.isNotEmpty() && sum > 5000) {
//                    val reason = "Label ${label.label} has uneven distribution for ranges: ${
//                        activated.map { "${it.start}-${it.end}" }.joinToString(";")
//                    }"
//                    if (alertEventsLastHour.firstOrNull { it.reason == reason } == null) {
//                        alertEvents.addAll(alerts.filter { it.labels.contains(label) }
//                            .map { AlertEvent(null, it, reason, AlertStatus.NEW, trace) })
//                    }
//
//                }
//            }

            alertEventRepository.saveAll(alertEvents)
//            alertEmailService.sendAlertEvents(alertEvents.toList())
        }
    }


}