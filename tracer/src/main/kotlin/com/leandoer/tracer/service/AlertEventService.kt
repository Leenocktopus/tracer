package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType
import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.alertevent.AlertEventFilter
import com.leandoer.tracer.model.dto.alertevent.GetAlertEventDto
import com.leandoer.tracer.model.dto.alertevent.PutEventAlertDto
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.repository.AlertEventRepository
import com.leandoer.tracer.repository.specification.AlertEventSpecification
import liquibase.pro.packaged.it
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AlertEventService(
    private val alertEventRepository: AlertEventRepository,
    private val alertEventSpecification: AlertEventSpecification
) {
    fun findAll(alertEventFilter: AlertEventFilter, pageable: Pageable): Page<GetAlertEventDto> {
        val specification = with(alertEventSpecification) {
            alertIdEquals(alertEventFilter.alertId).and(fetchTrace())
        }
        return alertEventRepository.findAll(specification, pageable).map { map(it) }
    }

    fun findByIdInternal(id: Int): AlertEvent {
        val specification = with(alertEventSpecification){idEquals(id).and(fetchTrace())}
        return alertEventRepository.findOne(specification).orElseThrow {
            throw ApiException(ErrorType.NOT_FOUND_EXCEPTION, mapOf("alertEventId" to id), "alert event has not been found")
        }
    }

    fun update(id: Int, eventAlert: PutEventAlertDto): GetAlertEventDto {
        val updatedAlert = findByIdInternal(id).copy(status = eventAlert.status)
        alertEventRepository.save(updatedAlert)
        return map(updatedAlert)
    }

}