package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.AlertEvent_
import com.leandoer.tracer.model.entity.Alert_
import com.leandoer.tracer.model.entity.Contact
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.model.entity.Trace_
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.criteria.JoinType


@Component
class AlertEventSpecification {

    fun fetchAlert(): Specification<AlertEvent> {
        return Specification<AlertEvent> { root, query, _ ->
            if (query.resultType == AlertEvent::class.java) { // prevent fetch for pagination count query
                root.fetch<AlertEvent, Alert>(AlertEvent_.ALERT)
            }
            null
        }
    }

    fun fetchTrace(): Specification<AlertEvent> {
        return Specification<AlertEvent> { root, query, _ ->
            if (query.resultType == AlertEvent::class.java) { // prevent fetch for pagination count query
                root.fetch<AlertEvent, Trace>(AlertEvent_.TRACE)
            }
            null
        }
    }

    fun alertIdEquals(alertId: Int?): Specification<AlertEvent> {
        return Specification<AlertEvent> { root, _, builder ->
            if (alertId == null){
                null
            } else {
                builder.equal(root.get<Int>(AlertEvent_.ALERT), alertId)
            }
        }
    }

    fun lastHour(): Specification<AlertEvent> {
        return Specification<AlertEvent> { root, _, builder ->
            val join = root.join<AlertEvent, Trace>(AlertEvent_.TRACE)
            builder.greaterThanOrEqualTo(join.get(Trace_.GENERATED_AT), LocalDateTime.now().minusHours(1))
        }
    }

    fun idEquals(id: Int?): Specification<AlertEvent> {
        return Specification<AlertEvent> { root, _, builder ->
            if (id == null){
                null
            } else {
                builder.equal(root.get<Int>(AlertEvent_.ID), id)
            }
        }
    }
}