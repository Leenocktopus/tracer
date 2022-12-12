package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.Alert_
import com.leandoer.tracer.model.entity.Contact
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.model.entity.Trace_
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.JoinType
import javax.xml.stream.Location


@Component
class AlertSpecification {
    fun mappedToLabel(labelId: Int?): Specification<Alert> {
        return Specification<Alert> { root, _, builder ->
            if (labelId == null){
                null
            } else {
                val join = root.join<Alert, Label>(Alert_.LABELS)
                builder.equal(join.get<Int>(Label_.ID), labelId)
            }

        }
    }

    fun mappedToLabels(labelIds: List<Int>): Specification<Alert> {
        return Specification<Alert> { root, _, builder ->
            if (labelIds.isEmpty()){
                null
            } else {
                val join = root.join<Alert, Label>(Alert_.LABELS)
                builder.and(join.get<Int>(Label_.ID).`in`(labelIds))
            }

        }
    }

    fun fetchContacts(): Specification<Alert> {
        return Specification<Alert> { root, query, _ ->
            if (query.resultType == Alert::class.java) { // prevent fetch for pagination count query
                root.fetch<Alert, Contact>(Alert_.CONTACTS, JoinType.LEFT)
            }
            null
        }
    }

    fun fetchLabels(): Specification<Alert> {
        return Specification<Alert> { root, query, _ ->
            if (query.resultType == Alert::class.java) { // prevent fetch for pagination count query
                root.fetch<Alert, Label>(Alert_.LABELS, JoinType.LEFT)
            }
            null
        }
    }

    fun idEquals(id: Int?): Specification<Alert> {
        return Specification<Alert> { root, _, builder ->
            if (id == null){
                null
            } else {
                builder.equal(root.get<Int>(Alert_.ID), id)
            }

        }
    }

    fun nameEquals(name: String?): Specification<Alert>? {
        return Specification<Alert> { root, _, builder ->
            if (name == null){
                null
            } else {
                builder.equal(root.get<Int>(Alert_.NAME), name)
            }
        }
    }

}