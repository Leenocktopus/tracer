package com.leandoer.tracer.mapper

import com.leandoer.tracer.model.dto.alert.GetAlertDto
import com.leandoer.tracer.model.dto.alert.PostAlertDto
import com.leandoer.tracer.model.dto.alert.PutAlertDto
import com.leandoer.tracer.model.dto.alertevent.GetAlertEventDto
import com.leandoer.tracer.model.dto.analytics.GetCountStatisticsDto
import com.leandoer.tracer.model.dto.analytics.GetTestStatisticsDto
import com.leandoer.tracer.model.dto.analytics.DistributionDto
import com.leandoer.tracer.model.dto.analytics.GetLabelDistributionDto
import com.leandoer.tracer.model.dto.analytics.RateDto
import com.leandoer.tracer.model.dto.application.GetApplicationDto
import com.leandoer.tracer.model.dto.label.GetLabelDto
import com.leandoer.tracer.model.dto.label.PostLabelDto
import com.leandoer.tracer.model.dto.test.GetTestDto
import com.leandoer.tracer.model.dto.testrun.GetTestRunDto
import com.leandoer.tracer.model.dto.trace.GetTraceDto
import com.leandoer.tracer.model.dto.trace.PostTraceDto
import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Contact
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Test
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.repository.projection.CountStatistics
import com.leandoer.tracer.repository.projection.TestStatistics
import com.leandoer.tracer.repository.projection.Distribution
import com.leandoer.tracer.repository.projection.LabelDistribution
import com.leandoer.tracer.repository.projection.Rate


fun map(trace: Trace) = GetTraceDto(
    id = trace.id!!,
    value = trace.value,
    application = trace.application.name,
    generatedAt = trace.generatedAt,
    labels = trace.labels.map { GetTraceDto.TraceLabelDto(it.id!!, it.label) }
)

fun map(trace: Rate) = RateDto(
    label = trace.label,
    timestamp = trace.timestamp,
    count = trace.count
)

fun map(trace: Distribution) = DistributionDto(
    application = trace.application,
    percentage = trace.percentage
)

fun map(count: CountStatistics) = GetCountStatisticsDto(
    applications = count.applications,
    labels = count.labels,
    traces = count.traces,
    alerts = count.alerts,
    alertEvents = count.alertEvents,
    alertEventsNew = count.alertEventsNew,
    alertEventsResolved = count.alertEventsResolved
)

fun map(count: TestStatistics) = GetTestStatisticsDto(
    testType = count.testType.title,
    type = count.type,
    count = count.count
)

fun map(labelDistribution: LabelDistribution) = GetLabelDistributionDto(
    start = labelDistribution.start,
    end = labelDistribution.end,
    count = labelDistribution.count
)

fun map(
    trace: PostTraceDto,
    fetchApplication: (application: String) -> Application,
    fetchLabel: (label: String) -> Label
) = Trace(
    value = trace.value,
    application = fetchApplication(trace.application),
    generatedAt = trace.generatedAt,
    labels = trace.labels.map { fetchLabel(it) }.toMutableSet()
)

fun map(label: Label) = GetLabelDto(
    id = label.id!!,
    label = label.label,
    tests = label.tests.map { GetLabelDto.LabelTestDto(it.id!!, it.testType.title, it.testType, it.type) },
    alerts = label.alerts.map { GetLabelDto.LabelAlertDto(it.id!!, it.name) }
)

fun map(label: PostLabelDto) = Label(
    label = label.label
)

fun map(application: Application) = GetApplicationDto(
    id = application.id!!,
    name = application.name
)

fun map(testRun: TestRun) = GetTestRunDto(
    id = testRun.id!!,
    test = testRun.test.testType.title,
    testResult = testRun.testResult,
    random = testRun.random
)

fun map(test: Test) = GetTestDto(
    id = test.id!!,
    title = test.testType.title,
    name = test.testType,
    type = test.type
)

fun map(alert: Alert) = GetAlertDto(
    id = alert.id!!,
    name = alert.name,
    contacts = alert.contacts.map { GetAlertDto.AlertContactDto(it.id!!, it.email) },
    labels = alert.labels.map { GetAlertDto.AlertLabelDto(it.id!!, it.label) },
)


fun map(alertDto: PostAlertDto): Alert {
    val alert = Alert(
        id = null,
        name = alertDto.name
    )
    alertDto.contacts.map { map(it, alert) }.forEach { alert.contacts.add(it) }
    return alert
}

fun map(contact: PutAlertDto.AlertContactDto, alert: Alert) = Contact(
    id = null,
    email = contact.email,
    alert = alert
)

fun map(contact: PostAlertDto.AlertContactDto, alert: Alert) = Contact(
    id = null,
    email = contact.email,
    alert = alert
)

fun map(it: AlertEvent) = GetAlertEventDto(
    id = it.id!!,
    reason = it.reason,
    traceId = it.trace.id!!,
    status = it.status,
    generatedAt = it.trace.generatedAt
)