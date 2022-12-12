package com.leandoer.tracer.service

import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.test.GetTestDto
import com.leandoer.tracer.model.dto.test.TestFilter
import com.leandoer.tracer.model.entity.Test
import com.leandoer.tracer.repository.TestRepository
import com.leandoer.tracer.repository.specification.TestSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TestService(
    val testRepository: TestRepository,
    val testSpecification: TestSpecification
) {
    fun findAll(testFilter: TestFilter, pageable: Pageable): Page<GetTestDto> {
        return testRepository.findAll(testSpecification.applyTestFilter(testFilter), pageable).map { map(it) }
    }

}
