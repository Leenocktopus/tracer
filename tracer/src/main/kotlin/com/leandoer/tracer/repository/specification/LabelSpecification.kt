package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.Test
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.JoinType


@Component
class LabelSpecification {
    fun idEquals(id: Int?): Specification<Label> {
        return Specification<Label> { root, _, builder ->
            if (id == null) {
                null
            } else {
                builder.equal(root.get<String>(Label_.ID), id)
            }
        }
    }

    fun labelEquals(label: String?): Specification<Label> {
        return Specification<Label> { root, _, builder ->
            if (label == null) {
                null
            } else {
                builder.equal(root.get<String>(Label_.LABEL), label)
            }
        }
    }

    fun idIn(ids: List<Int>): Specification<Label> {
        return Specification<Label> { root, _, builder ->
            if (ids.isNullOrEmpty()) {
                null
            } else {
                builder.and(root.get<String>(Label_.ID).`in`(ids))
            }
        }
    }

    fun fetchAlerts(): Specification<Label> {
        return Specification<Label> { root, query, _ ->
            if (query.resultType == Label::class.java) { // prevent fetch for pagination count query
                root.fetch<Label, Alert>(Label_.ALERTS, JoinType.LEFT)
            }
            null
        }
    }

    fun fetchTests(): Specification<Label> {
        return Specification<Label> { root, query, _ ->
            if (query.resultType == Label::class.java) { // prevent fetch for pagination count query
                root.fetch<Label, Test>(Label_.TESTS, JoinType.LEFT)
            }
            null
        }
    }
}