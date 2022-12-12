package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.dto.trace.TraceFilter
import com.leandoer.tracer.model.entity.Application_
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.model.entity.Trace_
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.criteria.JoinType


@Component
class TraceSpecification {

    fun applyTraceFilter(traceFilter: TraceFilter): Specification<Trace> {
        return applicationIn(traceFilter.applications)
            .and(labelsIn(traceFilter.labels))
            .and(greaterOrEqualThan(traceFilter.startTimestamp))
            .and(lessOrEqualThan(traceFilter.endTimestamp))
            .and(traceIdEquals(traceFilter.traceId))
    }

    fun applicationIn(application: List<String>): Specification<Trace> {
        return Specification<Trace> { root, _, builder ->
            if (application.isNullOrEmpty()) {
                null
            } else {
                val join = root.join<Trace, Label>(Trace_.APPLICATION)
                builder.and(join.get<String>(Application_.NAME).`in`(application))
            }
        }
    }

    fun applicationEquals(applicationName: String) = applicationIn(listOf(applicationName))

    fun labelsIn(labels: List<String>): Specification<Trace> {
        return Specification<Trace> { root, _, builder ->
            if (labels.isNullOrEmpty()) {
                null
            } else {
                val join = root.join<Trace, Label>(Trace_.LABELS, JoinType.LEFT)
                builder.and(join.get<List<Label>?>(Label_.LABEL).`in`(labels))
            }
        }
    }

    fun labelEquals(label: String) = labelsIn(listOf(label))

    fun traceIdEquals(traceId: Long?): Specification<Trace> {
        return Specification<Trace> { root, _, builder ->
            if (traceId == null) {
                null
            } else {
                builder.equal(root.get<Long>(Trace_.ID), traceId)
            }
        }
    }

    fun fetchTestRuns(): Specification<Trace> {
        return Specification<Trace> { root, query, builder ->
            if (query.resultType == Trace::class.java) { // prevent fetch for pagination count query
                root.fetch<Trace, TestRun>(Trace_.TEST_RUNS, JoinType.LEFT)
            }
            null
        }
    }

    fun greaterOrEqualThan(startTimestamp: LocalDateTime?): Specification<Trace> {
        return Specification<Trace> { root, _, builder ->
            if (startTimestamp == null) {
                null
            } else {
                builder.greaterThanOrEqualTo(
                    root.get(Trace_.GENERATED_AT),
                    builder.literal(startTimestamp)
                )
            }
        }
    }

    fun lessOrEqualThan(endTimestamp: LocalDateTime?): Specification<Trace> {
        return Specification<Trace> { root, _, builder ->
            if (endTimestamp == null) {
                null
            } else {
                builder.lessThanOrEqualTo(
                    root.get(Trace_.GENERATED_AT),
                    builder.literal(endTimestamp)
                )
            }
        }
    }

    fun fetchLabels(): Specification<Trace> {
        return Specification<Trace> { root, query, builder ->
            if (query.resultType == Trace::class.java) { // prevent fetch for pagination count query
                root.fetch<Trace, Label>(Trace_.LABELS, JoinType.LEFT)
            }
            null
        }
    }

    fun fetchApplications(): Specification<Trace> {
        return Specification<Trace> { root, query, builder ->
            if (query.resultType == Trace::class.java) { // prevent fetch for pagination count query
                root.fetch<Trace, TestRun>(Trace_.APPLICATION)
            }
            null
        }
    }
}