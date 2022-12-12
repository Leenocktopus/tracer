package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.alertevent.AlertEventFilter
import com.leandoer.tracer.model.dto.alertevent.GetAlertEventDto
import com.leandoer.tracer.model.dto.alertevent.PutEventAlertDto
import com.leandoer.tracer.model.entity.AlertEvent_.TRACE
import com.leandoer.tracer.model.entity.Trace_.GENERATED_AT
import com.leandoer.tracer.service.AlertEventService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/alert-events")
class AlertEventController(
    private val alertEventService: AlertEventService
) {

    @GetMapping
    fun findAll(
        @Valid alertEventFilter: AlertEventFilter,
        @PageableDefault(sort = ["${TRACE}.${GENERATED_AT}"], direction = DESC) pageable: Pageable
    ): Page<GetAlertEventDto> {
        return alertEventService.findAll(alertEventFilter, pageable)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable @Min(1) id: Int,
        @RequestBody @Valid eventAlert: PutEventAlertDto
    ): GetAlertEventDto {
        return alertEventService.update(id, eventAlert)
    }


}