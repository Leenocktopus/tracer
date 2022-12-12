package com.leandoer.tracer.service

import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.testrun.GetTestRunDto
import com.leandoer.tracer.model.dto.testrun.TestRunFilter
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.repository.TestRunRepository
import com.leandoer.tracer.repository.specification.TestRunSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TestRunService(
    private val testRunRepository: TestRunRepository,
    private val testRunSpecification: TestRunSpecification
) {

    fun findAll(testRunFilter: TestRunFilter, pageable: Pageable): Page<GetTestRunDto> {
        val specification = testRunSpecification.applyTestRunFilter(testRunFilter)
            .and(testRunSpecification.fetchTests())
        return testRunRepository.findAll(specification, pageable).map { map(it) }
    }

}
