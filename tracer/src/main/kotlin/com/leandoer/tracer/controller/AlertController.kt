package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.alert.AlertFilter
import com.leandoer.tracer.model.dto.alert.GetAlertDto
import com.leandoer.tracer.model.dto.alert.PostAlertDto
import com.leandoer.tracer.model.dto.alert.PutAlertDto
import com.leandoer.tracer.service.AlertService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/alerts")
class AlertController(
    private val alertService: AlertService
) {

    @GetMapping
    fun findAll(
        @Valid alertFilter: AlertFilter,
        @PageableDefault pageable: Pageable
    ): Page<GetAlertDto> {
        return alertService.findAll(alertFilter, pageable)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable @Min(1) id: Int): GetAlertDto {
        return alertService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody @Valid alert: PostAlertDto): GetAlertDto {
        return alertService.create(alert)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable @Min(1) id: Int, @RequestBody @Valid alert: PutAlertDto): GetAlertDto {
        return alertService.update(id, alert)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable @Min(1) id: Int) {
        alertService.delete(id)
    }
}