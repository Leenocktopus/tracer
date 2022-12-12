package com.leandoer.tracer.service.alerts

import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace

abstract class AlertProcessor(private var next: AlertProcessor?) {

    companion object {
        fun link(first: AlertProcessor, vararg chain: AlertProcessor): AlertProcessor {
            var head = first
            chain.forEach {
                head.next = it
                head = it
            }
            return first
        }
    }

    fun executeNext(trace: Trace, testRuns: List<TestRun>, alerts: List<Alert>): List<AlertEvent> {
        return executeTest(trace, testRuns, alerts) + (next?.executeNext(trace, testRuns, alerts) ?: listOf())
    }


    abstract fun executeTest(trace: Trace, testRuns: List<TestRun>, alerts: List<Alert>): List<AlertEvent>
}