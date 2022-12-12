package com.leandoer.tracer.service.alerts

import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.AlertStatus
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import liquibase.pro.packaged.it

class TestAlertProcessor : AlertProcessor(null) {

    override fun executeTest(trace: Trace, testRuns: List<TestRun>, alerts: List<Alert>): List<AlertEvent> {
        val reason = testRuns.groupBy { it.test.testType }
            .filter { (_, testRuns) -> testRuns.all { it.random == false } }.keys.joinToString(", ")
        println(reason)
        return if (reason.isNotBlank()){
            alerts.map { AlertEvent(null, it, "Тестування не пройдено: $reason.", AlertStatus.NEW, trace) }
        } else {
            listOf()
        }

    }
}