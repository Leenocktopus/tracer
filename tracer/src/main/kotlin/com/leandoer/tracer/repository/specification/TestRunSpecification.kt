package com.leandoer.tracer.repository.specification

import com.leandoer.tracer.model.dto.testrun.TestRunFilter
import com.leandoer.tracer.model.entity.Test
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.TestRun_
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.model.entity.Trace_
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component


@Component
class TestRunSpecification {

    fun traceEquals(traceId: Long?): Specification<TestRun> {
        return Specification<TestRun> { root, _, builder ->
            if (traceId == null) {
                null
            } else {
                builder.equal(root.get<Long>(TestRun_.TRACE), traceId)
            }
        }
    }

    fun applyTestRunFilter(testRunFilter: TestRunFilter): Specification<TestRun> {
        return traceEquals(testRunFilter.traceId)
    }

    fun fetchTests(): Specification<TestRun> {
        return Specification<TestRun> { root, query, builder ->
            if (query.resultType == TestRun::class.java) { // prevent fetch for pagination count query
                root.fetch<TestRun, Test>(TestRun_.TEST)
            }
            null
        }
    }
}