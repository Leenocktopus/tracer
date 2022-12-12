package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType.NOT_FOUND_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.CONSTRAINT_VIOLATION_EXCEPTION
import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.alert.AlertFilter
import com.leandoer.tracer.model.dto.alert.GetAlertDto
import com.leandoer.tracer.model.dto.alert.PostAlertDto
import com.leandoer.tracer.model.dto.alert.PutAlertDto
import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.repository.AlertRepository
import com.leandoer.tracer.repository.ContactRepository
import com.leandoer.tracer.repository.LabelRepository
import com.leandoer.tracer.repository.specification.AlertSpecification
import com.leandoer.tracer.repository.specification.LabelSpecification
import liquibase.pro.packaged.it
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlertService(
    private val alertRepository: AlertRepository,
    private val alertSpecification: AlertSpecification,
    private val contactRepository: ContactRepository,
    private val labelService: LabelService
) {

    fun findAll(alertFilter: AlertFilter, pageable: Pageable): Page<GetAlertDto> {
        val specification =
            with(alertSpecification) { mappedToLabel(alertFilter.labelId).and(fetchLabels().and(fetchContacts())) }
        return alertRepository.findAll(specification, pageable).map { map(it) }
    }

    fun findById(id: Int): GetAlertDto {
        return map(findByIdInternal(id))
    }

    fun findByIdInternal(id: Int): Alert {
        val specification = with(alertSpecification) { idEquals(id).and(fetchLabels()).and(fetchContacts()) }
        return alertRepository.findOne(specification).orElseThrow {
            throw ApiException(NOT_FOUND_EXCEPTION, mapOf("alertId" to id), "alert has not been found")
        }
    }

    @Transactional
    fun create(alert: PostAlertDto): GetAlertDto {
        alertRepository.findOne(alertSpecification.nameEquals(alert.name)).ifPresent {
            throw ApiException(
                CONSTRAINT_VIOLATION_EXCEPTION,
                mapOf("name" to alert.name),
                "alert already exists"
            )
        }
        val savedAlert = alertRepository.save(map(alert));
        return map(savedAlert.copy(contacts = contactRepository.saveAll(savedAlert.contacts).toMutableSet()))
    }

    @Transactional
    fun update(id: Int, alert: PutAlertDto): GetAlertDto {
        val alertFromDB = findByIdInternal(id)
        val alertContacts = alertFromDB.contacts.map { it.email }.toSet()
        contactRepository.saveAll(alert.contacts.filterNot { alertContacts.contains(it.email) }
            .map { map(it, alertFromDB) })
        val newContacts = alert.contacts.map { it.email }
        contactRepository.deleteAll(alertFromDB.contacts.filterNot { newContacts.contains(it.email) })
        val labels = labelService.findByIdIn(alert.labels)
        alertFromDB.labels.retainAll { alert.labels.contains(it.id) }
        labels.forEach { alertFromDB.labels.add(it) }
        return map(alertRepository.save(alertFromDB))
    }

    @Transactional
    fun delete(id: Int) {
        val alert = alertRepository.findOne(alertSpecification.idEquals(id)).orElseThrow {
            throw ApiException(NOT_FOUND_EXCEPTION, mapOf("alertId" to id), "alert has not been found")
        }
        contactRepository.deleteAll(alert.contacts)
        alertRepository.deleteById(id)
    }

}
