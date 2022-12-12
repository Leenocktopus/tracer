package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.label.GetLabelDto
import com.leandoer.tracer.model.dto.label.PostLabelDto
import com.leandoer.tracer.model.dto.label.PutLabelDto
import com.leandoer.tracer.model.entity.Label_
import com.leandoer.tracer.service.LabelService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
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
@RequestMapping("/labels")
class LabelController(
    private val labelService: LabelService
) {

    @GetMapping
    fun findAll(
        @PageableDefault(sort = [Label_.ID]) pageable: Pageable
    ): Page<GetLabelDto> {
        return labelService.findAll(pageable)
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable @Min(1) id: Int,
    ): GetLabelDto {
        return labelService.findById(id)
    }

    @ResponseStatus(CREATED)
    @PostMapping
    fun create(@RequestBody @Valid label: PostLabelDto): GetLabelDto {
        return labelService.create(label)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable @Min(1) id: Int, @RequestBody @Valid label: PutLabelDto): GetLabelDto {
        return labelService.update(id, label)
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable @Min(1) id: Int) {
        labelService.delete(id)
    }
}