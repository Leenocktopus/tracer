package com.leandoer.tracer.service

import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.application.GetApplicationDto
import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.repository.ApplicationRepository
import com.leandoer.tracer.repository.specification.ApplicationSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ApplicationService(
    val applicationRepository: ApplicationRepository,
    val applicationSpecification: ApplicationSpecification
) {

    fun findAll(pageable: Pageable): Page<GetApplicationDto> {
        return applicationRepository.findAll(pageable).map { map(it) };
    }

    fun findByName(application: String): Application? {
        return applicationRepository.findOne(applicationSpecification.nameEquals(application)).orElse(null)
    }

    fun create(application: String): Application {
        return applicationRepository.save(Application(null, application))
    }

}
