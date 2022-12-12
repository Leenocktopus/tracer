package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType
import com.leandoer.tracer.exception.ErrorType.CONSTRAINT_VIOLATION_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.NOT_FOUND_EXCEPTION
import com.leandoer.tracer.mapper.map
import com.leandoer.tracer.model.dto.label.GetLabelDto
import com.leandoer.tracer.model.dto.label.PostLabelDto
import com.leandoer.tracer.model.dto.label.PutLabelDto
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.repository.LabelRepository
import com.leandoer.tracer.repository.TestRepository
import com.leandoer.tracer.repository.specification.LabelSpecification
import liquibase.pro.packaged.it
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LabelService(
    val labelRepository: LabelRepository,
    val labelSpecification: LabelSpecification,
    val testRepository: TestRepository
) {


    fun findAll(pageable: Pageable): Page<GetLabelDto> {
        val specification = with(labelSpecification) { fetchAlerts().and(fetchTests()) }
        return labelRepository.findAll(specification, pageable).map { map(it) };
    }

    fun findById(id: Int): GetLabelDto {
        return map(findByIdInternal(id))
    }

    fun findByLabel(label: String): Label {
        val specification = with(labelSpecification) {
            labelEquals(label)
                .and(fetchTests())
                .and(fetchAlerts())
        }
        return labelRepository.findOne(specification).orElseThrow {
            throw ApiException(NOT_FOUND_EXCEPTION, mapOf("label" to label), "label has not been found")
        }
    }

    fun findByIdInternal(id: Int): Label {
        val specification = with(labelSpecification) {
            idEquals(id)
                .and(fetchTests())
                .and(fetchAlerts())
        }
        return labelRepository.findOne(specification).orElseThrow {
            throw ApiException(NOT_FOUND_EXCEPTION, mapOf("labelId" to id), "label has not been found")
        }
    }

    fun findByIdIn(ids: List<Int>): List<Label> {
        val specification = with(labelSpecification) {
            idIn(ids)
        }
        val labels = labelRepository.findAll(specification)
        val labelIdSet = labels.map { it.id }.toSet()
        ids.filterNot { labelIdSet.contains(it) }.firstOrNull()?.let {
            throw ApiException(NOT_FOUND_EXCEPTION, mapOf("labelId" to it), "label has not been found")
        }
        return labels
    }

    @Transactional
    fun create(label: PostLabelDto): GetLabelDto {
        labelRepository.findOne(labelSpecification.labelEquals(label.label)).ifPresent {
            throw ApiException(CONSTRAINT_VIOLATION_EXCEPTION, mapOf("label" to label.label), "label already exists")
        }
        return map(labelRepository.save(map(label)))
    }

    @Transactional
    fun update(id: Int, label: PutLabelDto): GetLabelDto {
        val labelFromDB = findByIdInternal(id)
        val tests = testRepository.findAll().filter { it.id in label.tests }.toMutableSet()

        tests.filter { it.parametrized }.let {
            if (it.isNotEmpty()) throw
            ApiException(
                CONSTRAINT_VIOLATION_EXCEPTION,
                mapOf("testIds" to it.map { it.id }),
                "parametrized tests can't be added to labels"
            )
        }

        return map(labelRepository.save(labelFromDB.copy(tests = tests)))
    }

    @Transactional
    fun delete(id: Int) {
        findById(id).apply {
            if (this.alerts.isNotEmpty()) throw ApiException(
                CONSTRAINT_VIOLATION_EXCEPTION,
                mapOf("label" to label),
                "can't delete label with mapped alerts"
            )
        }
        labelRepository.deleteById(id)
    }
}
