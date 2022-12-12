package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.dto.test.TestFilter
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.model.entity.Test
import com.leandoer.tracer.model.entity.Test_
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.JoinType


@Component
class TestSpecification {

    fun applyTestFilter(testFilter: TestFilter): Specification<Test> {
        return isParametrized(testFilter.parametrized)
    }


    fun isParametrized(parametrized: Boolean?): Specification<Test> {
        return Specification<Test> { root, _, builder ->
            if (parametrized == null) {
                null
            } else {
                builder.equal(root.get<String>(Test_.PARAMETRIZED), parametrized)
            }
        }
    }

    fun hasLabel(label: String?): Specification<Test> {
        return Specification<Test> { root, _, builder ->
            if (label == null) {
                null
            } else {
                val join = root.join<Test, Label>(Test_.LABELS, JoinType.LEFT)
                builder.equal(join.get<String>(Label_.LABEL), label)
            }
        }
    }

    fun doesntHaveLabel(label: String?): Specification<Test> {
        return Specification<Test> { root, _, builder ->
            if (label == null) {
                null
            } else {
                val join = root.join<Test, Label>(Test_.LABELS, JoinType.LEFT)
                builder.not(builder.equal(join.get<String>(Label_.LABEL), label))
            }
        }
    }
}